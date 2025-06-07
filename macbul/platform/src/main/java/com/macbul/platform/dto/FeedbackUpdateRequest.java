// src/main/java/com/macbul/platform/dto/FeedbackUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated on existing feedback.
 */
@Data
public class FeedbackUpdateRequest {
    private Integer rating;   // optional
    private String comment;   // optional
}
