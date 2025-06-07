// src/main/java/com/macbul/platform/repository/MatchVideoRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.MatchVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list by match for MatchVideo.
 */
public interface MatchVideoRepository extends JpaRepository<MatchVideo, String> {

    /**
     * List all videos for a given match.
     */
    List<MatchVideo> findByMatchId(String matchId);
}
