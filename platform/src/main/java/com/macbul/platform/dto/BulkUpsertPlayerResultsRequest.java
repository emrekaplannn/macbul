// src/main/java/com/macbul/platform/dto/result/BulkUpsertPlayerResultsRequest.java
package com.macbul.platform.dto;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BulkUpsertPlayerResultsRequest {
    private String matchId;
    private List<UpsertPlayerResultRequest> players;
}
