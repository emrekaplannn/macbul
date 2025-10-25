// src/main/java/com/macbul/platform/service/MatchTeamResultService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.model.MatchTeamResult;
import com.macbul.platform.repository.MatchTeamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class MatchTeamResultService {

    private final MatchTeamResultRepository repo;

    public List<MatchTeamResultDto> listByMatch(String matchId){
        return repo.findByMatchId(matchId).stream().map(this::toDto).toList();
    }

    @Transactional
    public MatchTeamResultDto upsertTeamScore(UpsertTeamScoreRequest req){
        var m = repo.findByMatchIdAndTeamLabel(req.getMatchId(), req.getTeamLabel())
                .orElseGet(() -> MatchTeamResult.builder()
                        .matchId(req.getMatchId())
                        .teamLabel(req.getTeamLabel())
                        .score(0).isWinner(false).build());
        if (req.getScore()!=null)    m.setScore(req.getScore());
        if (req.getIsWinner()!=null) m.setIsWinner(req.getIsWinner());
        return toDto(repo.save(m));
    }

    @Transactional
    public List<MatchTeamResultDto> bulkUpsert(BulkUpsertTeamScoresRequest req){
        return req.getTeams().stream().map(this::upsertTeamScore).toList();
    }

    private MatchTeamResultDto toDto(MatchTeamResult t){
        return MatchTeamResultDto.builder()
                .matchTeamResultId(t.getMatchTeamResultId())
                .matchId(t.getMatchId())
                .teamLabel(t.getTeamLabel())
                .score(t.getScore())
                .isWinner(t.getIsWinner())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
