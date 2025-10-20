package com.macbul.platform.controller;

import com.macbul.platform.dto.ProfileMeResponse;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.UserRepository;
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

    public ProfileController(SecurityUtils securityUtils, UserRepository userRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
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

        // DTO dön
        return ResponseEntity.ok(
                ProfileMeResponse.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .emailVerified(Boolean.TRUE.equals(u.getEmailVerified()))
                        .isBanned(Boolean.TRUE.equals(u.getIsBanned()))
                        .build()
        );
    }
}
