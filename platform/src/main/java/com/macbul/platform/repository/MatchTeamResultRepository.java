// src/main/java/com/macbul/platform/repository/MatchTeamResultRepository.java
package com.macbul.platform.repository;
import com.macbul.platform.model.MatchTeamResult;
import com.macbul.platform.util.TeamLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MatchTeamResultRepository extends JpaRepository<MatchTeamResult, Long> {
    List<MatchTeamResult> findByMatchId(String matchId);
    Optional<MatchTeamResult> findByMatchIdAndTeamLabel(String matchId, TeamLabel teamLabel);
    void deleteByMatchId(String matchId);
}
