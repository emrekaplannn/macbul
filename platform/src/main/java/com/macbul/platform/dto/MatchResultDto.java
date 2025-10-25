package com.macbul.platform.dto;
import com.macbul.platform.util.MatchStatus;
import com.macbul.platform.util.WinningTeam;

import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchResultDto {
    private Long   matchResultId;
    private String matchId;
    private MatchStatus status;
    private Long   startedAt;
    private Long   endedAt;
    private WinningTeam winningTeam;   // A/B/DRAW
    private String notes;
    private Long   createdAt;
    private Long   updatedAt;
}
