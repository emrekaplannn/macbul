// src/main/java/com/macbul/platform/model/ReferralCode.java
package com.macbul.platform.model;

import com.macbul.platform.util.ReferralCodeStatus;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity for a user’s referral code.
 */
@Entity
@Table(
    name = "referral_codes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_referral_codes_code", columnNames = "code")
    },
    indexes = {
        @Index(name = "idx_referral_codes_user", columnList = "user_id")
    }
)
@Data
public class ReferralCode {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private ReferralCodeStatus status = ReferralCodeStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
