// src/main/java/com/macbul/platform/dto/TransactionDto.java
package com.macbul.platform.dto;

import com.macbul.platform.util.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO returned for a transaction.
 */
@Data
public class TransactionDto {
    private String id;
    private String userId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Long createdAt;
}
