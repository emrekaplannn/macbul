// src/main/java/com/macbul/platform/dto/MatchParticipantUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

/**
 * Fields that can be updated for an existing MatchParticipant.
 */
@Data
public class MatchParticipantUpdateRequest {

    private String teamId;     // optional
    private Boolean hasPaid;   // optional
}
