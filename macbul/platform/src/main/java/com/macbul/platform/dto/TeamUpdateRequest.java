// src/main/java/com/macbul/platform/dto/TeamUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated on an existing Team.
 * Only non-null values will be applied.
 */
@Data
public class TeamUpdateRequest {

    /** New average score. */
    private Integer averageScore;
}
