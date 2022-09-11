package com.shefin.ipldashboard.data;


import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shefin.ipldashboard.model.Team;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  @Autowired
  private final EntityManager em;

  public JobCompletionNotificationListener(EntityManager em) {
	this.em = em;
	}

  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");
      
      Map<String, Team> teamData = new HashMap<String, Team>();
      

      em.createQuery("select m.team1, count(*) from Match m group by m.team1",Object[].class)
      .getResultList()
      .stream()
      .map(e -> new Team((String) e[0], (long) e[1]))
      .forEach(t -> teamData.put(t.getTeamName(), t));
      
      em.createQuery("select m.team1, count(*) from Match m group by m.team1",Object[].class)
      .getResultList()
      .stream()
      .forEach(e ->{
    	  if (teamData.get((String) e[0])!=null) {
    		  Team team = teamData.get((String) e[0]);
    	      team.setTotalMatches(team.getTotalMatches()+ (long) e[1]);
    	  }
    	 else {
    		 Team team = new Team((String) e[0], (long) e[1]);
    		 teamData.put(team.getTeamName(), team);
    		 team.setTotalMatches(team.getTotalMatches()+ (long) e[1]);
    	 }
      	
      });
      
      em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner	",Object[].class)
      .getResultList()
      .stream()
      .forEach(ef ->{
    		  Team team = teamData.get(ef[0]);
    		  if (team != null) team.setTotalWins((long) ef[1]);
    	  
    	  
    	  
      });
      
      teamData.values().forEach(team -> em.persist(team));
      
      System.out.println(teamData.get("Delhi Capitals"));
      
    }
  }
}