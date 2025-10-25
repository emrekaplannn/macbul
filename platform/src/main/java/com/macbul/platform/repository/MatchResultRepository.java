// src/main/java/com/macbul/platform/repository/MatchResultRepository.java
package com.macbul.platform.repository;
import com.macbul.platform.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    Optional<MatchResult> findByMatchId(String matchId);
    boolean existsByMatchId(String matchId);
}
