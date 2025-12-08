// RegisterRequest.java
package com.macbul.platform.dto.auth;
import com.macbul.platform.util.City;
import com.macbul.platform.util.PlayerPosition;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min=6) String password,
        String fullName,
        String phone,
        PlayerPosition position,
        String avatarPath,
        String referralCode,
        Integer overallScore,
        City city,        // örn: "İstanbul"
        String district    // örn: "Kadıköy" — ya da null
        ) {}
