// src/main/java/com/macbul/platform/model/Team.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing a Team within a Match.
 */
@Entity
@Table(
    name = "teams",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_teams_match_team",
            columnNames = {"match_id", "team_number"}
        )
    },
    indexes = {
        @Index(name = "idx_teams_match", columnList = "match_id")
    }
)
@Data
public class Team {

    /** Primary key (UUID). */
    @Id
    @Column(name = "id", length = 36)
    private String id;

    /**
     * Many teams belong to one Match.
     * Replace Match with your actual entity if it exists.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    /** Team number within the match (e.g., 1 or 2). */
    @Column(name = "team_number", nullable = false)
    private Integer teamNumber;

    /** Average score (can be null until computed). */
    @Column(name = "average_score")
    private Integer averageScore;

    /** Creation timestamp (epoch millis). */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
