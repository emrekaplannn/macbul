// src/main/java/com/macbul/platform/model/Notification.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for a user notification.
 */
@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(name = "idx_notifications_user", columnList = "user_id")
    }
)
@Data
public class Notification {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /** The user who receives this notification */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Categorized type of this notification */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private com.macbul.platform.util.NotificationType type;

    /** JSON or text payload with extra info */
    @Column(name = "payload", columnDefinition = "LONGTEXT")
    private String payload;

    /** Read status */
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    /** Epoch millis when created */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
