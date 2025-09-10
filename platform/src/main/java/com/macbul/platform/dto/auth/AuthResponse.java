// AuthResponse.java
package com.macbul.platform.dto.auth;
public record AuthResponse(String accessToken, String refreshToken, String tokenType, long expiresInMs) {}
