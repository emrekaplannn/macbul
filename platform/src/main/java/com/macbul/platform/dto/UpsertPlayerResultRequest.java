// src/main/java/com/macbul/platform/dto/result/UpsertPlayerResultRequest.java
package com.macbul.platform.dto;
import com.macbul.platform.util.*;

import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpsertPlayerResultRequest {
    private String matchId;
    private String userId;
    private TeamLabel teamLabel;
    private AttendanceStatus attendanceStatus; // default PLAYED
    private PlayerPosition position;
    private Integer goals;
    private Integer assists;
    private Integer ownGoals;
    private Integer saves;
    private Integer rating; // 0-100
    private Boolean mvp;
    private String notes;
}
