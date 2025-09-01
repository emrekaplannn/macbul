// src/main/java/com/macbul/platform/dto/ReferralCodeUpdateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.ReferralCodeStatus;

import lombok.Data;

/**
 * Fields that can be updated on an existing referral code.
 */
@Data
public class ReferralCodeUpdateRequest {
    private String code;
    private ReferralCodeStatus status;
}
