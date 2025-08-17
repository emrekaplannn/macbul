// src/main/java/com/macbul/platform/dto/VideoClipDto.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for a video clip.
 */
@Data
public class VideoClipDto {
    private String id;
    private String matchVideoId;
    private String userId;
    private String clipUrl;
    private Integer startSec;
    private Integer endSec;
    private Long createdAt;
}
