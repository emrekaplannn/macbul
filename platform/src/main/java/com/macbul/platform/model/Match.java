// src/main/java/com/macbul/platform/model/Match.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * JPA entity for a Match.
 */
@Entity
@Table(
    name = "matches",
    indexes = {
        @Index(name = "idx_matches_timestamp", columnList = "match_timestamp"),
        @Index(name = "fk_matches_users", columnList = "organizer_id"),
        @Index(name = "fk_matches_district", columnList = "location")
    }
)
@Data
public class Match {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    /**
     * The User who organized this match.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "field_name", length = 100, nullable = false)
    private String fieldName;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    /**
     * Location → FK to district.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location") // nullable olabilir, kullanıcı konum seçmeyebilir.
    private District district;

    /**
     * Epoch millis when the match starts.
     */
    @Column(name = "match_timestamp", nullable = false)
    private Long matchTimestamp;

    @Column(name = "price_per_user", precision = 8, scale = 2, nullable = false)
    private BigDecimal pricePerUser;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    /**
     * Epoch millis when this record was created.
     */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
