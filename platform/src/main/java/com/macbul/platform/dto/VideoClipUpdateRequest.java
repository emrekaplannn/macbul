// src/main/java/com/macbul/platform/dto/VideoClipUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated on an existing video clip.
 */
@Data
public class VideoClipUpdateRequest {
    private String clipUrl;
    private Integer startSec;
    private Integer endSec;
}
