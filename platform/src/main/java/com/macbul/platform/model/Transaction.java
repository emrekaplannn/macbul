// src/main/java/com/macbul/platform/model/Transaction.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.macbul.platform.util.TransactionType;

/**
 * JPA entity for a financial transaction.
 */
@Entity
@Table(
    name = "transactions",
    indexes = {
        @Index(name = "idx_transactions_user", columnList = "user_id")
    }
)
@Data
public class Transaction {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, nullable = false)
    private TransactionType type;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;
}
