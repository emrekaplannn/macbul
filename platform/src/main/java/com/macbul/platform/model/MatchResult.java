// src/main/java/com/macbul/platform/model/MatchResult.java
package com.macbul.platform.model;

import com.macbul.platform.util.MatchStatus;
import com.macbul.platform.util.WinningTeam;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "match_result",
        uniqueConstraints = @UniqueConstraint(name="uq_match_result__match", columnNames = "match_id"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_result_id")
    private Long matchResultId;

    @Column(name = "match_id", length = 36, nullable = false)
    private String matchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    @Builder.Default
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(name = "started_at")
    private Long startedAt;

    @Column(name = "ended_at")
    private Long endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "winning_team", length = 8)
    @Builder.Default
    private WinningTeam winningTeam = WinningTeam.DRAW;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    @PrePersist void prePersist(){ long now=System.currentTimeMillis(); createdAt=now; updatedAt=now; }
    @PreUpdate  void preUpdate(){  updatedAt=System.currentTimeMillis(); }
}
