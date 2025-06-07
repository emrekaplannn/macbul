// src/main/java/com/macbul/platform/dto/VideoClipCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to create a new video clip.
 */
@Data
public class VideoClipCreateRequest {
    private String matchVideoId;
    private String userId;
    private String clipUrl;
    private Integer startSec;
    private Integer endSec;
    private Long createdAt;  // optional; if null, service will set to now
}
