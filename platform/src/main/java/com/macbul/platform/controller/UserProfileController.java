// src/main/java/com/macbul/platform/controller/UserProfileController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.UserProfileCreateRequest;
import com.macbul.platform.dto.UserProfileDto;
import com.macbul.platform.dto.UserProfileUpdateRequest;
import com.macbul.platform.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user profiles.
 */
@Tag(name = "UserProfile API", description = "Endpoints for creating, reading, updating, and deleting user profiles")
@RestController
@RequestMapping("/v1/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService profileService;

    @Operation(summary = "Create new profile", description = "Creates a profile for an existing user")
    @PostMapping
    public ResponseEntity<UserProfileDto> createProfile(@RequestBody UserProfileCreateRequest request) {
        UserProfileDto created = profileService.createProfile(request);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get profile by userId", description = "Returns the profile data for the given userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable String userId) {
        UserProfileDto dto = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "List all profiles", description = "Returns all user profiles")
    @GetMapping
    public ResponseEntity<List<UserProfileDto>> listProfiles() {
        List<UserProfileDto> all = profileService.getAllProfiles();
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "Update profile", description = "Updates the profile fields for the specified userId")
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable String userId,
            @RequestBody UserProfileUpdateRequest request
    ) {
        UserProfileDto updated = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete profile", description = "Deletes the profile for the given userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
}
