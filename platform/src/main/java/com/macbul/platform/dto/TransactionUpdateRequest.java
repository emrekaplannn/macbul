// src/main/java/com/macbul/platform/dto/TransactionUpdateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields that can be updated in an existing transaction.
 */
@Data
public class TransactionUpdateRequest {
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Long createdAt;
}
