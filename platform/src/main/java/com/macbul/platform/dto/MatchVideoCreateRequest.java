// src/main/java/com/macbul/platform/dto/MatchVideoCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to upload a new video to a match.
 */
@Data
public class MatchVideoCreateRequest {
    private String matchId;
    private String videoUrl;
    private Long uploadedAt; // optional; if null, service will set to now
}
