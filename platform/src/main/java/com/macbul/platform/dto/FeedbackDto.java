// src/main/java/com/macbul/platform/dto/FeedbackDto.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for feedback.
 */
@Data
public class FeedbackDto {
    private String id;
    private String matchId;
    private String userId;
    private Integer rating;
    private String comment;
    private Long createdAt;
}
