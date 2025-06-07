// src/main/java/com/macbul/platform/dto/ReferralUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields that can be updated in an existing Referral.
 */
@Data
public class ReferralUpdateRequest {
    private BigDecimal rewardAmount;
    private Boolean rewarded;
}
