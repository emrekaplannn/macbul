// src/main/java/com/macbul/platform/model/MatchTeamResult.java
package com.macbul.platform.model;

import com.macbul.platform.util.TeamLabel;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "match_team_result",
        uniqueConstraints = @UniqueConstraint(name="uq_match_team_result__match_team", columnNames = {"match_id","team_label"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchTeamResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_team_result_id")
    private Long matchTeamResultId;

    @Column(name = "match_id", length = 36, nullable = false)
    private String matchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_label", nullable = false, length = 8)
    private TeamLabel teamLabel; // A/B

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "is_winner", nullable = false)
    private Boolean isWinner;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    @PrePersist void prePersist(){ long now=System.currentTimeMillis(); createdAt=now; updatedAt=now; }
    @PreUpdate  void preUpdate(){  updatedAt=System.currentTimeMillis(); }
}
