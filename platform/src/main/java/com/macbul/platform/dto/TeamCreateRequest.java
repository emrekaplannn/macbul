// src/main/java/com/macbul/platform/dto/TeamCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to create a new Team.
 */
@Data
public class TeamCreateRequest {

    /** UUID of the existing Match. */
    private String matchId;

    /** Team number within the match (e.g., 1 or 2). */
    private Integer teamNumber;
}
