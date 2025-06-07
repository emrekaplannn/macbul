// src/main/java/com/macbul/platform/service/TeamService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {

    @Autowired private TeamRepository teamRepo;
    @Autowired private MatchRepository matchRepo;      // or use plain matchId string
    @Autowired private MapperUtil mapper;

    /** Create a new Team. */
    public TeamDto createTeam(TeamCreateRequest req) {
        // Verify Match exists
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));

        // Ensure (matchId + teamNumber) is unique
        if (teamRepo.existsByMatchIdAndTeamNumber(req.getMatchId(), req.getTeamNumber())) {
            throw new IllegalArgumentException(
                "Team " + req.getTeamNumber() + " already exists for match " + req.getMatchId()
            );
        }

        Team team = new Team();
        team.setId(UUID.randomUUID().toString());
        team.setMatch(match);
        team.setTeamNumber(req.getTeamNumber());
        team.setCreatedAt(System.currentTimeMillis());
        // averageScore stays null by default

        Team saved = teamRepo.save(team);
        return mapper.toTeamDto(saved);
    }

    /** Get single Team by ID. */
    public TeamDto getTeamById(String id) {
        Team team = teamRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));
        return mapper.toTeamDto(team);
    }

    /** List all Teams. */
    public List<TeamDto> getAllTeams() {
        return teamRepo.findAll()
            .stream()
            .map(mapper::toTeamDto)
            .collect(Collectors.toList());
    }

    /** List Teams by Match ID. */
    public List<TeamDto> getTeamsByMatchId(String matchId) {
        return teamRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toTeamDto)
            .collect(Collectors.toList());
    }

    /** Update averageScore on an existing Team. */
    public TeamDto updateTeam(String id, TeamUpdateRequest req) {
        Team team = teamRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));

        if (req.getAverageScore() != null) {
            team.setAverageScore(req.getAverageScore());
        }

        Team updated = teamRepo.save(team);
        return mapper.toTeamDto(updated);
    }

    /** Delete a Team by ID. */
    public void deleteTeam(String id) {
        if (!teamRepo.existsById(id)) {
            throw new ResourceNotFoundException("Team not found: " + id);
        }
        teamRepo.deleteById(id);
    }
}
