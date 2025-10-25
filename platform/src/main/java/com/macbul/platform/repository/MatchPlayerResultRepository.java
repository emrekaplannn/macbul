// src/main/java/com/macbul/platform/repository/MatchPlayerResultRepository.java
package com.macbul.platform.repository;
import com.macbul.platform.model.MatchPlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MatchPlayerResultRepository extends JpaRepository<MatchPlayerResult, Long> {
    List<MatchPlayerResult> findByMatchId(String matchId);
    List<MatchPlayerResult> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<MatchPlayerResult> findByMatchIdAndUserId(String matchId, String userId);
    void deleteByMatchId(String matchId);
}
