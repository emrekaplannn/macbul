// src/main/java/com/macbul/platform/dto/result/MatchTeamResultDto.java
package com.macbul.platform.dto;
import com.macbul.platform.util.TeamLabel;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchTeamResultDto {
    private Long matchTeamResultId;
    private String matchId;
    private TeamLabel teamLabel;
    private Integer score;
    private Boolean isWinner;
    private Long createdAt;
    private Long updatedAt;
}
