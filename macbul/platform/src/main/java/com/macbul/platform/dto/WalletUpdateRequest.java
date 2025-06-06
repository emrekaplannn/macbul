// src/main/java/com/macbul/platform/dto/WalletUpdateRequest.java
package com.macbul.platform.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Fields that can be updated for an existing Wallet.
 * Only non-null fields will be applied.
 */
@Data
public class WalletUpdateRequest {

    /**
     * New balance (e.g. after deposit or withdrawal).
     */
    private BigDecimal balance;

    /**
     * Timestamp of the update (epoch millis). If null, Service will set current time.
     */
    private Long updatedAt;
}
