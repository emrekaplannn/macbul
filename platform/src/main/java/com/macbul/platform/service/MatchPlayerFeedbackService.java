// src/main/java/com/macbul/service/MatchPlayerFeedbackService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.model.MatchPlayerFeedback;
import com.macbul.platform.repository.MatchPlayerFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class MatchPlayerFeedbackService {

    private final MatchPlayerFeedbackRepository repo;

    @Transactional
    public FeedbackResponse upsert(FeedbackUpsertRequest req) {
        if (req.reviewerId().equals(req.targetId())) {
            throw new ResponseStatusException(BAD_REQUEST, "Kullanıcı kendini puanlayamaz.");
        }
        if (req.overallRating() < 0 || req.overallRating() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Puan 0–5 arasında olmalı.");
        }

        var now = Instant.now().toEpochMilli();

        var entity = repo.findByMatchIdAndReviewerIdAndTargetId(
                req.matchId(), req.reviewerId(), req.targetId()
        ).map(existing -> {
            existing.setOverallRating((short) req.overallRating());
            existing.setComment(req.comment());
            existing.setUpdatedAt(now);
            return existing;
        }).orElseGet(() -> MatchPlayerFeedback.builder()
                .matchId(req.matchId())
                .reviewerId(req.reviewerId())
                .targetId(req.targetId())
                .overallRating((short) req.overallRating())
                .comment(req.comment())
                .createdAt(now)
                .updatedAt(now)
                .build());

        var saved = repo.save(entity);
        return new FeedbackResponse(
                saved.getId(), saved.getMatchId(), saved.getReviewerId(), saved.getTargetId(),
                saved.getOverallRating(), saved.getComment(), saved.getCreatedAt(), saved.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> listByMatch(String matchId) {
        return repo.findByMatchIdOrderByCreatedAtDesc(matchId).stream()
                .map(f -> new FeedbackResponse(
                        f.getId(), f.getMatchId(), f.getReviewerId(), f.getTargetId(),
                        f.getOverallRating(), f.getComment(), f.getCreatedAt(), f.getUpdatedAt()
                )).toList();
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> listReceivedByUser(String userId) {
        return repo.findByTargetIdOrderByCreatedAtDesc(userId).stream()
                .map(f -> new FeedbackResponse(
                        f.getId(), f.getMatchId(), f.getReviewerId(), f.getTargetId(),
                        f.getOverallRating(), f.getComment(), f.getCreatedAt(), f.getUpdatedAt()
                )).toList();
    }

    @Transactional(readOnly = true)
    public PlayerMatchRatingSummary getPlayerMatchSummary(String matchId, String targetId) {
        var all = repo.findByMatchIdOrderByCreatedAtDesc(matchId).stream()
                .filter(f -> f.getTargetId().equals(targetId)).toList();
        long votes = all.size();
        double avg = votes == 0 ? 0.0 : all.stream().mapToInt(f -> f.getOverallRating()).average().orElse(0.0);
        return new PlayerMatchRatingSummary(matchId, targetId, avg, votes);
    }
}
