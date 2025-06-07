// src/main/java/com/macbul/platform/repository/FeedbackRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-match and list-by-user for Feedback.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    List<Feedback> findByMatchId(String matchId);

    List<Feedback> findByUserId(String userId);
}
