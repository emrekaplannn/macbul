// src/main/java/com/macbul/platform/dto/result/BulkUpsertTeamScoresRequest.java
package com.macbul.platform.dto;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BulkUpsertTeamScoresRequest {
    private String matchId;
    private List<UpsertTeamScoreRequest> teams; // A ve B birlikte gelebilir
}
