// src/main/java/com/macbul/platform/model/MatchPlayerResult.java
package com.macbul.platform.model;

import com.macbul.platform.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "match_player_result",
        uniqueConstraints = @UniqueConstraint(name="uq_match_player_result__match_user", columnNames = {"match_id","user_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchPlayerResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_player_result_id")
    private Long matchPlayerResultId;

    @Column(name = "match_id", length = 36, nullable = false)
    private String matchId;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_label", nullable = false, length = 8)
    private TeamLabel teamLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false, length = 16)
    @Builder.Default
    private AttendanceStatus attendanceStatus = AttendanceStatus.PLAYED;

    @Column(name = "position", length = 16)
    private String position; // GK/DEF/MID/FWD (serbest)

    @Column(name = "goals", nullable = false)
    private Integer goals;

    @Column(name = "assists", nullable = false)
    private Integer assists;

    @Column(name = "own_goals", nullable = false)
    private Integer ownGoals;

    @Column(name = "saves", nullable = false)
    private Integer saves;

    // 0-100 kontrolünü servis tarafında uygularız.
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "mvp", nullable = false)
    private Boolean mvp;

    @Column(name = "notes", length = 512)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    @PrePersist void prePersist(){ long now=System.currentTimeMillis(); createdAt=now; updatedAt=now; }
    @PreUpdate  void preUpdate(){  updatedAt=System.currentTimeMillis(); }
}
