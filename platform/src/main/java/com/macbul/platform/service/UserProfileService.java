// src/main/java/com/macbul/platform/service/UserProfileService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.UserProfileDto;
import com.macbul.platform.dto.UserProfileUpdateRequest;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.User;
import com.macbul.platform.model.UserProfile;
import com.macbul.platform.repository.UserProfileRepository;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for creating, reading, updating, and deleting user profiles.
 */
@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperUtil mapperUtil;

    /**
     * Create a new UserProfile for an existing User.
     */

    public UserProfileDto createProfile(UserProfileCreateRequest request, String userId) {
        // Verify the User exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // If profile already exists, prevent duplicate
        if (userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("Profile already exists for user: " + userId);
        }

        // Build entity (PK = userId, mapped from user via @MapsId)
        UserProfile profile = new UserProfile();
        profile.setUser(user); // @MapsId → userId otomatik eşleşir
        profile.setFullName(request.getFullName());
        profile.setPosition(request.getPosition());
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setBio(request.getBio());

        // Save and return DTO
        UserProfile saved = userProfileRepository.save(profile);
        return mapperUtil.toUserProfileDto(saved);
    }

    /**
     * Get a single UserProfile by userId.
     */
    public UserProfileDto getProfileByUserId(String userId) {
        UserProfile profile = userProfileRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));
        return mapperUtil.toUserProfileDto(profile);
    }

    /**
     * Return a list of all UserProfiles.
     */
    public List<UserProfileDto> getAllProfiles() {
        return userProfileRepository.findAll()
            .stream()
            .map(mapperUtil::toUserProfileDto)
            .collect(Collectors.toList());
    }

    /**
     * Update an existing UserProfile by userId.
     */
    public UserProfileDto updateProfile(String userId, UserProfileUpdateRequest request) {
        UserProfile existing = userProfileRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

        // Only non-null fields are modified
        if (request.getFullName() != null) {
            existing.setFullName(request.getFullName());
        }
        if (request.getPosition() != null) {
            existing.setPosition(request.getPosition());
        }
        if (request.getAvatarUrl() != null) {
            existing.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            existing.setBio(request.getBio());
        }

        UserProfile updated = userProfileRepository.save(existing);
        return mapperUtil.toUserProfileDto(updated);
    }

    /**
     * Delete a UserProfile by userId.
     */
    public void deleteProfile(String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Profile not found for user: " + userId);
        }
        userProfileRepository.deleteById(userId);
    }
}
