// src/main/java/com/macbul/platform/dto/ReportCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to file a new report.
 */
@Data
public class ReportCreateRequest {
    private String matchId;
    private String reporterUserId;
    private String reportedUserId;
    private String reason;
    private String details;    // optional
    private Long createdAt;    // optional; defaults to now
}
