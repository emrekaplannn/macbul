// src/main/java/com/macbul/platform/service/MatchParticipantService.java
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
public class MatchParticipantService {

    @Autowired private MatchParticipantRepository mpRepo;
    @Autowired private MatchRepository            matchRepo;
    @Autowired private UserRepository             userRepo;
    @Autowired private TeamRepository             teamRepo;
    @Autowired private MapperUtil                 mapper;

    /** Create a new participation record */
    public MatchParticipantDto create(MatchParticipantCreateRequest req) {
        // verify match & user
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));
        User user = userRepo.findById(req.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));

        // enforce one participation per user/match
        if (mpRepo.existsByMatchIdAndUserId(match.getId(), user.getId())) {
            throw new IllegalArgumentException(
                "User " + user.getId() + " already joined match " + match.getId()
            );
        }

        MatchParticipant mp = new MatchParticipant();
        mp.setId(UUID.randomUUID().toString());
        mp.setMatch(match);
        mp.setUser(user);

        // optional team
        if (req.getTeamId() != null) {
            Team team = teamRepo.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + req.getTeamId()));
            mp.setTeam(team);
        }

        mp.setJoinedAt(
            req.getJoinedAt() != null 
                ? req.getJoinedAt() 
                : System.currentTimeMillis()
        );
        mp.setHasPaid(
            req.getHasPaid() != null 
                ? req.getHasPaid() 
                : Boolean.FALSE
        );

        return mapper.toMatchParticipantDto(mpRepo.save(mp));
    }

    /** Get by ID */
    public MatchParticipantDto getById(String id) {
        MatchParticipant mp = mpRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Participation not found: " + id));
        return mapper.toMatchParticipantDto(mp);
    }

    /** List all */
    public List<MatchParticipantDto> getAll() {
        return mpRepo.findAll()
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** List by match */
    public List<MatchParticipantDto> getByMatchId(String matchId) {
        return mpRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** List by user */
    public List<MatchParticipantDto> getByUserId(String userId) {
        return mpRepo.findByUserId(userId)
            .stream()
            .map(mapper::toMatchParticipantDto)
            .collect(Collectors.toList());
    }

    /** Update team assignment or payment status */
    public MatchParticipantDto update(String id, MatchParticipantUpdateRequest req) {
        MatchParticipant mp = mpRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Participation not found: " + id));

        if (req.getTeamId() != null) {
            Team team = teamRepo.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + req.getTeamId()));
            mp.setTeam(team);
        }
        if (req.getHasPaid() != null) {
            mp.setHasPaid(req.getHasPaid());
        }

        return mapper.toMatchParticipantDto(mpRepo.save(mp));
    }

    /** Delete */
    public void delete(String id) {
        if (!mpRepo.existsById(id)) {
            throw new ResourceNotFoundException("Participation not found: " + id);
        }
        mpRepo.deleteById(id);
    }
}
