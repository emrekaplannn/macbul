// src/main/java/com/macbul/platform/dto/MatchVideoDto.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for a match video.
 */
@Data
public class MatchVideoDto {
    private String id;
    private String matchId;
    private String videoUrl;
    private Long uploadedAt;
}
