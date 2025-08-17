package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated for an existing user.
 * If a field is null, it will not be changed.
 */
@Data
public class UserUpdateRequest {
    private String email;
    private String phone;
    private String referredByCode;
}
