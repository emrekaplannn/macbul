// src/main/java/com/macbul/platform/model/Feedback.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for user feedback on a match.
 */
@Entity
@Table(
    name = "feedback",
    indexes = {
        @Index(name = "idx_feedback_match", columnList = "match_id"),
        @Index(name = "idx_feedback_user",  columnList = "user_id")
    }
)
@Data
public class Feedback {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /** The match this feedback refers to */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    /** The user who left this feedback */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Rating value, e.g. 1–5 */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /** Optional textual comment */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /** Epoch millis when feedback was created */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
