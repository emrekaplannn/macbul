// src/main/java/com/macbul/platform/repository/TeamRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-match operations for Team.
 */
public interface TeamRepository extends JpaRepository<Team, String> {

    /** List all teams in a given match. */
    List<Team> findByMatchId(String matchId);

    /** Check uniqueness of (matchId + teamNumber). */
    boolean existsByMatchIdAndTeamNumber(String matchId, Integer teamNumber);
}
