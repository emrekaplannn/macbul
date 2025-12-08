package com.macbul.platform.dto;

import com.macbul.platform.util.PlayerPosition;

import lombok.Data;

/**
 * Fields that can be updated in an existing UserProfile.
 * Any null field will be left unchanged.
 */
@Data
public class UserProfileUpdateRequest {

    private String fullName;
    private PlayerPosition position;
    private String avatarPath;
    private String bio;
    private Integer districtId;
}
