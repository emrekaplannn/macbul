// src/main/java/com/macbul/platform/service/MatchResultService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.model.MatchResult;
import com.macbul.platform.repository.MatchResultRepository;
import com.macbul.platform.repository.MatchTeamResultRepository;
import com.macbul.platform.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MatchResultService {

    private final MatchResultRepository matchResultRepo;
    private final MatchTeamResultRepository teamRepo;

    public MatchResultDto getByMatchId(String matchId){
        var mr = matchResultRepo.findByMatchId(matchId).orElse(null);
        return mr==null? null : toDto(mr);
    }

    @Transactional
    public MatchResultDto upsert(UpsertMatchResultRequest req){
        var mr = matchResultRepo.findByMatchId(req.getMatchId()).orElseGet(() ->
                MatchResult.builder().matchId(req.getMatchId()).status(MatchStatus.SCHEDULED).build());

        if (req.getStatus()!=null) mr.setStatus(req.getStatus());
        if (req.getStartedAt()!=null) mr.setStartedAt(req.getStartedAt());
        if (req.getEndedAt()!=null) mr.setEndedAt(req.getEndedAt());
        if (req.getWinningTeam()!=null) mr.setWinningTeam(req.getWinningTeam());
        if (req.getNotes()!=null) mr.setNotes(req.getNotes());

        return toDto(matchResultRepo.save(mr));
    }

    /** Finalize: skorları okuyup winningTeam set eder, status COMPLETED yapar */
    @Transactional
    public MatchResultDto finalizeMatch(String matchId){
        var a = teamRepo.findByMatchIdAndTeamLabel(matchId, TeamLabel.A).orElse(null);
        var b = teamRepo.findByMatchIdAndTeamLabel(matchId, TeamLabel.B).orElse(null);

        var mr = matchResultRepo.findByMatchId(matchId).orElseGet(() ->
                MatchResult.builder().matchId(matchId).status(MatchStatus.SCHEDULED).build());

        WinningTeam winning = WinningTeam.DRAW;
        if (a != null && b != null) {
            if (a.getScore() > b.getScore()) winning = WinningTeam.A;
            else if (b.getScore() > a.getScore()) winning = WinningTeam.B;
        }
        mr.setWinningTeam(winning);
        mr.setStatus(MatchStatus.COMPLETED);
        return toDto(matchResultRepo.save(mr));
    }

    private MatchResultDto toDto(MatchResult m){
        return MatchResultDto.builder()
                .matchResultId(m.getMatchResultId())
                .matchId(m.getMatchId())
                .status(m.getStatus())
                .startedAt(m.getStartedAt())
                .endedAt(m.getEndedAt())
                .winningTeam(m.getWinningTeam())
                .notes(m.getNotes())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}
