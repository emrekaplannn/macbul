package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for a UserProfile.
 */
@Data
public class UserProfileDto {

    private String userId;
    private String fullName;
    private String position;
    private String avatarUrl;
    private String bio;
}
