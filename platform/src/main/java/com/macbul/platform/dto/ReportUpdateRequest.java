// src/main/java/com/macbul/platform/dto/ReportUpdateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.ReportStatus;
import lombok.Data;

/**
 * Fields that can be updated on an existing report.
 */
@Data
public class ReportUpdateRequest {
    private ReportStatus status;   // optional
    private Long resolvedAt;       // optional; set when status moves to RESOLVED or DISMISSED
}
