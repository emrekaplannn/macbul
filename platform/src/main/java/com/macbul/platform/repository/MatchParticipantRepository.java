// src/main/java/com/macbul/platform/repository/MatchParticipantRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + listing by match or user for MatchParticipant.
 */
public interface MatchParticipantRepository 
        extends JpaRepository<MatchParticipant, String> {

    List<MatchParticipant> findByMatchId(String matchId);
    List<MatchParticipant> findByUserId(String userId);

    boolean existsByMatchIdAndUserId(String matchId, String userId);

    long countByMatchIdAndHasPaidTrue(String matchId);

}
