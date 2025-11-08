// src/main/java/com/macbul/service/MatchFeedbackService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.MatchFeedbackResponse;
import com.macbul.platform.dto.MatchFeedbackUpsertRequest;
import com.macbul.platform.model.MatchFeedback;
import com.macbul.platform.repository.MatchFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class MatchFeedbackService {

    private final MatchFeedbackRepository repo;

    @Transactional
    public MatchFeedbackResponse upsert(String userId, MatchFeedbackUpsertRequest req) {
        if (req.organizationQuality() < 0 || req.organizationQuality() > 5
                || req.facilityQuality() < 0 || req.facilityQuality() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Puanlar 0–5 arasında olmalı.");
        }

        var now = Instant.now().toEpochMilli();

        var entity = repo.findByMatchIdAndUserId(req.matchId(), userId)
                .map(ex -> {
                    ex.setOrganizationQuality((short) req.organizationQuality());
                    ex.setFacilityQuality((short) req.facilityQuality());
                    ex.setFairPlay(req.fairPlay());
                    ex.setPaymentAppExperience(req.paymentAppExperience());
                    ex.setComment(req.comment());
                    ex.setUpdatedAt(now);
                    return ex;
                })
                .orElseGet(() -> MatchFeedback.builder()
                        .matchId(req.matchId())
                        .userId(userId)
                        .organizationQuality((short) req.organizationQuality())
                        .facilityQuality((short) req.facilityQuality())
                        .fairPlay(req.fairPlay())
                        .paymentAppExperience(req.paymentAppExperience())
                        .comment(req.comment())
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

        var saved = repo.save(entity);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<MatchFeedbackResponse> listByMatch(String matchId) {
        return repo.findByMatchIdOrderByCreatedAtDesc(matchId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<MatchFeedbackResponse> listByUser(String userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDto).toList();
    }

    private MatchFeedbackResponse toDto(MatchFeedback f) {
        return new MatchFeedbackResponse(
                f.getId(),
                f.getMatchId(),
                f.getUserId(),
                f.getOrganizationQuality(),
                f.getFacilityQuality(),
                f.getFairPlay(),
                f.getPaymentAppExperience(),
                f.getComment(),
                f.getCreatedAt(),
                f.getUpdatedAt()
        );
    }
}
