// src/main/java/com/macbul/platform/dto/TransactionCreateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields required to create a new transaction.
 */
@Data
public class TransactionCreateRequest {
    private String userId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;    // optional
    private Long createdAt;        // optional; if null, service will set now
}
