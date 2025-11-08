// src/main/java/com/macbul/repository/MatchPlayerFeedbackRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.MatchPlayerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchPlayerFeedbackRepository
        extends JpaRepository<MatchPlayerFeedback, Long> {

    Optional<MatchPlayerFeedback> findByMatchIdAndReviewerIdAndTargetId(
            String matchId, String reviewerId, String targetId);

    List<MatchPlayerFeedback> findByMatchIdOrderByCreatedAtDesc(String matchId);

    List<MatchPlayerFeedback> findByTargetIdOrderByCreatedAtDesc(String targetId);
}
