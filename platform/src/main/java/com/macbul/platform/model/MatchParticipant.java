// src/main/java/com/macbul/platform/model/MatchParticipant.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for a user’s participation in a match.
 */
@Entity
@Table(
    name = "match_participants",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_mp_match_user",
            columnNames = {"match_id", "user_id"}
        )
    },
    indexes = {
        @Index(name = "idx_mp_match", columnList = "match_id"),
        @Index(name = "idx_mp_user",  columnList = "user_id"),
        @Index(name = "fk_mp_teams",  columnList = "team_id")
    }
)
@Data
public class MatchParticipant {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "joined_at", nullable = false)
    private Long joinedAt;

    @Column(name = "has_paid", nullable = false)
    private Boolean hasPaid = false;
}
