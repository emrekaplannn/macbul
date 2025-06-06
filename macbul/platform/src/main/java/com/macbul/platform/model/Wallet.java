// src/main/java/com/macbul/platform/model/Wallet.java
package com.macbul.platform.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing a user’s Wallet.
 * Each User can have exactly one Wallet (unique user_id).
 */
@Entity
@Table(
    name = "wallets",
    indexes = {
        @Index(name = "idx_wallets_user", columnList = "user_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_wallets_user", columnNames = "user_id")
    }
)
@Data
public class Wallet {

    /**
     * Primary key (UUID string).
     */
    @Id
    @Column(name = "id", length = 36)
    private String id;

    /**
     * Reference to the owning User. One-to-one, since user_id is unique.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Current balance. Defaults to 0.00.
     */
    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal balance;

    /**
     * Timestamp (epoch millis) of the last update.
     */
    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;
}
