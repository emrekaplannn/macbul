// RegisterRequest.java
package com.macbul.platform.dto.auth;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min=6) String password,
        String fullName,
        String phone,
        String position,
        String avatarPath,
        String referralCode,
        Integer overallScore,
        String location
) {}
