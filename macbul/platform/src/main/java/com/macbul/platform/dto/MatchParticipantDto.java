// src/main/java/com/macbul/platform/dto/MatchParticipantDto.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * DTO returned to clients for a MatchParticipant.
 */
@Data
public class MatchParticipantDto {

    private String id;
    private String matchId;
    private String userId;
    private String teamId;
    private Long joinedAt;
    private Boolean hasPaid;
}
