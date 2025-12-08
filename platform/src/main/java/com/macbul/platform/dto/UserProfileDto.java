package com.macbul.platform.dto;

import com.macbul.platform.util.PlayerPosition;

import lombok.Data;

/**
 * DTO returned to clients for a UserProfile.
 */
@Data
public class UserProfileDto {

    private String userId;
    private String fullName;
    private PlayerPosition position;
    private String avatarPath;
    private String bio;
    private Integer districtId;
}
