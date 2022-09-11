package com.shefin.ipldashboard.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shefin.ipldashboard.model.Team;
import com.shefin.ipldashboard.repository.MatchRepository;
import com.shefin.ipldashboard.repository.TeamRepository;

@RestController
@CrossOrigin
public class TeamController{
	
	private TeamRepository teamRepository;
	private MatchRepository matchRepository;
	
	
	public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
		super();
		this.teamRepository = teamRepository;
		this.matchRepository = matchRepository;
	}


	@GetMapping("/team/{teamName}")
	public Team getTeam(@PathVariable String teamName) {
		Team team = this.teamRepository.findByTeamName(teamName);
		team.setLatestMatches(this.matchRepository.findLatestMatchesByTeam(teamName, 4));
		
		return team;
	}
	

}
