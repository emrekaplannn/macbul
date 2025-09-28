// src/main/java/com/macbul/platform/dto/MatchSlotsDto.java
package com.macbul.platform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchSlotsDto {
    private String matchId;
    private Integer totalSlots;
    private Long paidCount;
    private Integer remaining; // totalSlots - paidCount (0 altına inmez)
    private Boolean full;      // paidCount >= totalSlots
}
