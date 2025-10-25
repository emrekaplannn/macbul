// src/main/java/com/macbul/platform/dto/result/UpsertTeamScoreRequest.java
package com.macbul.platform.dto;
import com.macbul.platform.util.TeamLabel;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpsertTeamScoreRequest {
    private String matchId;
    private TeamLabel teamLabel; // A/B
    private Integer score;
    private Boolean isWinner;    // servis finalize ederken de set edebilir
}
