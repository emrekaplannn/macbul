// src/main/java/com/macbul/platform/dto/ReferralCodeDto.java
package com.macbul.platform.dto;

import com.macbul.platform.util.ReferralCodeStatus;

import lombok.Data;

/**
 * DTO returned to clients for a referral code.
 */
@Data
public class ReferralCodeDto {
    private String id;
    private String userId;
    private String code;
    private ReferralCodeStatus status;
    private Long createdAt;
}
