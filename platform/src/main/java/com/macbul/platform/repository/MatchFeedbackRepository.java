// src/main/java/com/macbul/repository/MatchFeedbackRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.MatchFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchFeedbackRepository extends JpaRepository<MatchFeedback, Long> {

    Optional<MatchFeedback> findByMatchIdAndUserId(String matchId, String userId);

    List<MatchFeedback> findByMatchIdOrderByCreatedAtDesc(String matchId);

    List<MatchFeedback> findByUserIdOrderByCreatedAtDesc(String userId);
}
