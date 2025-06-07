// src/main/java/com/macbul/platform/dto/MatchParticipantCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields required to create a MatchParticipant.
 */
@Data
public class MatchParticipantCreateRequest {

    private String matchId;
    private String userId;
    private String teamId;    // optional
    private Long joinedAt;    // optional, defaults to now
    private Boolean hasPaid;  // optional, defaults to false
}
