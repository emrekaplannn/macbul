package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to create a new UserProfile. 
 * The userId must reference an existing User.
 */
@Data
public class UserProfileCreateRequest {

    /**
     * UUID of an existing User. 
     * This will become the primary key of the new profile.
     */
    private String fullName;
    private String position;
    private String avatarUrl;
    private String bio;
}
