// src/main/java/com/macbul/platform/dto/ReferralDto.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO returned to clients for a Referral.
 */
@Data
public class ReferralDto {
    private String id;
    private String referrerUserId;
    private String referredUserId;
    private String matchId;
    private BigDecimal rewardAmount;
    private Boolean rewarded;
    private Long createdAt;
}
