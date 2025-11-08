// src/main/java/com/macbul/model/MatchPlayerFeedback.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_player_feedback",
       uniqueConstraints = @UniqueConstraint(name = "ux_match_reviewer_target",
           columnNames = {"match_id","reviewer_id","target_id"}),
       indexes = {
           @Index(name="idx_match", columnList="match_id"),
           @Index(name="idx_reviewer", columnList="reviewer_id"),
           @Index(name="idx_target", columnList="target_id")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchPlayerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="match_id", nullable=false, length=36)
    private String matchId;

    @Column(name="reviewer_id", nullable=false, length=36)
    private String reviewerId;

    @Column(name="target_id", nullable=false, length=36)
    private String targetId;

    @Column(name="overall_rating", nullable=false)
    private short overallRating; // 0–5

    @Column(name="comment", length=255)
    private String comment;

    @Column(name="created_at", nullable=false)
    private Long createdAt; // epoch millis

    @Column(name="updated_at")
    private Long updatedAt; // epoch millis
}
