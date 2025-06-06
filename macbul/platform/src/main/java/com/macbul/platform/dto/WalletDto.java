// src/main/java/com/macbul/platform/dto/WalletDto.java
package com.macbul.platform.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * DTO returned to clients for a Wallet.
 */
@Data
public class WalletDto {

    private String id;
    private String userId;
    private BigDecimal balance;
    private Long updatedAt;
}
