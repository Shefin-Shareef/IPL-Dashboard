package com.shefin.ipldashboard.data;


import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;


public class MatchDataProcessor implements ItemProcessor<MatchInput, com.shefin.ipldashboard.model.Match> {

  private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

  @Override
  public com.shefin.ipldashboard.model.Match process(final MatchInput matchInput) throws Exception {
	  
    com.shefin.ipldashboard.model.Match match = new com.shefin.ipldashboard.model.Match();
    match.setId(Long.parseLong(matchInput.getId()));
    match.setCity(matchInput.getCity());
    match.setDate(LocalDate.parse(matchInput.getDate()));
    match.setPlayerOfMatch(matchInput.getPlayer_of_match());
    match.setVenue(matchInput.getVenue());
    match.setNeutral_venue(matchInput.getNeutral_venue());
    
    String firstInningsTeam, secondInningsTeam;
    
    if ("bat".equals(matchInput.getToss_decision())) {
		firstInningsTeam = matchInput.getToss_winner();
		secondInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
		
	}
    else {
		secondInningsTeam = matchInput.getToss_winner();
		firstInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
	}
    
    match.setTeam1(firstInningsTeam);
    match.setTeam2(secondInningsTeam);
    
    match.setTossWinner(matchInput.getToss_winner());
    match.setTossDecision(matchInput.getToss_decision());
    match.setMatchWinner(matchInput.getWinner());
    
    match.setResult(matchInput.getResult());
    match.setResult_margin(matchInput.getResult_margin());
    
    match.setUmpire1(matchInput.getUmpire1());
    match.setUmpire2(matchInput.getUmpire2());
    return match;
  }

}

