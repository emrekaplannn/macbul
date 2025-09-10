// LoginRequest.java
package com.macbul.platform.dto.auth;
import jakarta.validation.constraints.*;

public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
