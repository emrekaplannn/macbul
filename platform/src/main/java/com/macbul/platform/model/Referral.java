// src/main/java/com/macbul/platform/model/Referral.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * JPA entity for a referral record.
 */
@Entity
@Table(
    name = "referrals",
    indexes = {
        @Index(name = "idx_referrals_referrer", columnList = "referrer_user_id"),
        @Index(name = "idx_referrals_referred", columnList = "referred_user_id"),
        @Index(name = "idx_referrals_match", columnList = "match_id")
    }
)
@Data
public class Referral {

    /** Primary key (UUID). */
    @Id
    @Column(name = "id", length = 36)
    private String id;

    /** The user who made the referral. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referrer_user_id", nullable = false)
    private User referrerUser;

    /** The user who was referred. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referred_user_id", nullable = false)
    private User referredUser;

    /** Optional match context for this referral. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    /** Reward amount (nullable until set). */
    @Column(name = "reward_amount", precision = 10, scale = 2)
    private BigDecimal rewardAmount;

    /** Whether reward has been granted. Defaults to false. */
    @Column(name = "rewarded", nullable = false)
    private Boolean rewarded = false;

    /** Timestamp (epoch millis) when this record was created. */
    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
