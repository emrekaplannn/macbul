// src/main/java/com/macbul/platform/dto/result/UpsertMatchResultRequest.java
package com.macbul.platform.dto;
import com.macbul.platform.util.MatchStatus;
import com.macbul.platform.util.WinningTeam;

import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpsertMatchResultRequest {
    private String matchId;
    private MatchStatus status;     // optional
    private Long   startedAt;       // optional
    private Long   endedAt;         // optional
    private WinningTeam winningTeam;  // A | B | DRAW
    private String notes;           // optional
}
