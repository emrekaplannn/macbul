// src/main/java/com/macbul/platform/dto/ReferralCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields required to create a new Referral.
 */
@Data
public class ReferralCreateRequest {
    private String referrerUserId;
    private String referredUserId;
    private String matchId;           // optional
    private BigDecimal rewardAmount;  // optional
    private Boolean rewarded;         // optional
    private Long createdAt;           // optional; if null, service will set now
}
