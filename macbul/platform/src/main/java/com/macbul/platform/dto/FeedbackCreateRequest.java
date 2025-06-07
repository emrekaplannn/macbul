// src/main/java/com/macbul/platform/dto/FeedbackCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to submit new feedback.
 */
@Data
public class FeedbackCreateRequest {
    private String matchId;
    private String userId;
    private Integer rating;
    private String comment;   // optional
    private Long createdAt;   // optional; if null, service will set to now
}
