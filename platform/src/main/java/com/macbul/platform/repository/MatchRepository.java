// src/main/java/com/macbul/platform/repository/MatchRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-organizer operations for Match.
 */
public interface MatchRepository extends JpaRepository<Match, String> {

    /**
     * List all matches organized by a given user.
     */
    List<Match> findByOrganizerId(String organizerId);
}
