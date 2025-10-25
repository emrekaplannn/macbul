// src/main/java/com/macbul/platform/service/MatchPlayerResultService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.model.MatchPlayerResult;
import com.macbul.platform.repository.MatchPlayerResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class MatchPlayerResultService {

    private final MatchPlayerResultRepository repo;

    public List<MatchPlayerResultDto> listByMatch(String matchId){
        return repo.findByMatchId(matchId).stream().map(this::toDto).toList();
    }

    public List<MatchPlayerResultDto> historyByUser(String userId){
        return repo.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public MatchPlayerResultDto upsert(UpsertPlayerResultRequest r){
        if (r.getRating()!=null && (r.getRating()<0 || r.getRating()>100))
            throw new IllegalArgumentException("rating must be between 0 and 100");

        var m = repo.findByMatchIdAndUserId(r.getMatchId(), r.getUserId())
                .orElseGet(() -> MatchPlayerResult.builder()
                        .matchId(r.getMatchId())
                        .userId(r.getUserId())
                        .teamLabel(r.getTeamLabel())
                        .goals(0).assists(0).ownGoals(0).saves(0)
                        .mvp(false)
                        .build());

        if (r.getTeamLabel()!=null)       m.setTeamLabel(r.getTeamLabel());
        if (r.getAttendanceStatus()!=null)m.setAttendanceStatus(r.getAttendanceStatus());
        if (r.getPosition()!=null)        m.setPosition(r.getPosition());
        if (r.getGoals()!=null)           m.setGoals(r.getGoals());
        if (r.getAssists()!=null)         m.setAssists(r.getAssists());
        if (r.getOwnGoals()!=null)        m.setOwnGoals(r.getOwnGoals());
        if (r.getSaves()!=null)           m.setSaves(r.getSaves());
        if (r.getRating()!=null)          m.setRating(r.getRating());
        if (r.getMvp()!=null)             m.setMvp(r.getMvp());
        if (r.getNotes()!=null)           m.setNotes(r.getNotes());

        return toDto(repo.save(m));
    }

    @Transactional
    public List<MatchPlayerResultDto> bulkUpsert(BulkUpsertPlayerResultsRequest req){
        return req.getPlayers().stream().map(this::upsert).toList();
    }

    private MatchPlayerResultDto toDto(MatchPlayerResult p){
        return MatchPlayerResultDto.builder()
                .matchPlayerResultId(p.getMatchPlayerResultId())
                .matchId(p.getMatchId())
                .userId(p.getUserId())
                .teamLabel(p.getTeamLabel())
                .attendanceStatus(p.getAttendanceStatus())
                .position(p.getPosition())
                .goals(p.getGoals())
                .assists(p.getAssists())
                .ownGoals(p.getOwnGoals())
                .saves(p.getSaves())
                .rating(p.getRating())
                .mvp(p.getMvp())
                .notes(p.getNotes())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
