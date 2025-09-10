// src/main/java/com/macbul/platform/dto/WalletCreateRequest.java
package com.macbul.platform.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Fields required to create a new Wallet.
 * - userId: must refer to an existing User.
 * - Optionally, an initial balance (default is 0.00 if omitted).
 */
@Data
public class WalletCreateRequest {

    private BigDecimal initialBalance;
}
