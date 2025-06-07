// src/main/java/com/macbul/platform/dto/ReportDto.java
package com.macbul.platform.dto;

import com.macbul.platform.util.ReportStatus;
import lombok.Data;

/**
 * DTO returned to clients for a report.
 */
@Data
public class ReportDto {
    private String id;
    private String matchId;
    private String reporterUserId;
    private String reportedUserId;
    private String reason;
    private String details;
    private ReportStatus status;
    private Long createdAt;
    private Long resolvedAt;
}
