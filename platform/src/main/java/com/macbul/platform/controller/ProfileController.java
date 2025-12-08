package com.macbul.platform.controller;

import com.macbul.platform.dto.ProfileMeResponse;
import com.macbul.platform.model.User;
import com.macbul.platform.model.UserProfile;
import com.macbul.platform.repository.UserProfileRepository;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.util.City;
import com.macbul.platform.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile API")
@RestController
@RequestMapping("/v1/profile")
public class ProfileController {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public ProfileController(SecurityUtils securityUtils, UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Operation(summary = "Get current user's profile (by auth token)")
    @GetMapping("/me")
    public ResponseEntity<ProfileMeResponse> me() {
        // Token'dan userId oku
        final String userId = securityUtils.getCurrentUserId();
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        // DB’den kullanıcıyı getir
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile up = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));


         // District bilgisi
        String locationString = null;

        if (up.getDistrict() != null) {
            City city = up.getDistrict().getCity();           // İstanbul
            String districtName = up.getDistrict().getDistrictName();   // Kadıköy

            if (districtName != null && !districtName.isBlank()) {
                locationString = districtName + ", " + city;    // Kadıköy, İstanbul
            } else {
                locationString = city.name();                          // İstanbul
            }
        }

        // DTO dön
        return ResponseEntity.ok(
                ProfileMeResponse.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .emailVerified(Boolean.TRUE.equals(u.getEmailVerified()))
                        .isBanned(Boolean.TRUE.equals(u.getIsBanned()))
                        .overall(u.getOverallScore())
                        .fullName(up.getFullName())
                        .position(up.getPosition() != null ? up.getPosition().getLabel() : null)
                        .location(locationString)
                        .bio(up.getBio())
                        .build()
        );
    }
}