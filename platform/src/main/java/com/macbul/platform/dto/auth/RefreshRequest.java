// RefreshRequest.java
package com.macbul.platform.dto.auth;
import jakarta.validation.constraints.NotBlank;
public record RefreshRequest(@NotBlank String refreshToken) {}
