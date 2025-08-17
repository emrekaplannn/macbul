// src/main/java/com/macbul/platform/dto/MatchVideoUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated on an existing MatchVideo.
 */
@Data
public class MatchVideoUpdateRequest {
    private String videoUrl;
}
