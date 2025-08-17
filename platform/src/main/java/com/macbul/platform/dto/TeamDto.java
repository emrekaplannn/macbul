// src/main/java/com/macbul/platform/dto/TeamDto.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for a Team.
 */
@Data
public class TeamDto {

    private String id;
    private String matchId;
    private Integer teamNumber;
    private Integer averageScore;
    private Long createdAt;
}
