// src/main/java/com/macbul/platform/model/Report.java
package com.macbul.platform.model;

import com.macbul.platform.util.ReportStatus;
import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for a user report on a match.
 */
@Entity
@Table(
    name = "reports",
    indexes = {
        @Index(name = "idx_reports_match",    columnList = "match_id"),
        @Index(name = "idx_reports_reporter", columnList = "reporter_user_id"),
        @Index(name = "idx_reports_reported", columnList = "reported_user_id")
    }
)
@Data
public class Report {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_user_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reported;

    @Column(name = "reason", length = 255, nullable = false)
    private String reason;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ReportStatus status = ReportStatus.NEW;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "resolved_at")
    private Long resolvedAt;
}
