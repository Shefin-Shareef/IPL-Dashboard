package com.shefin.ipldashboard.repository;

import org.springframework.data.repository.CrudRepository;

import com.shefin.ipldashboard.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long>{
	
	Team findByTeamName(String teamName);
}
