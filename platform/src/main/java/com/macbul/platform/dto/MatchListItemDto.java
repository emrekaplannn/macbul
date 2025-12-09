// src/main/java/com/macbul/platform/dto/MatchListItemDto.java
package com.macbul.platform.dto;

import lombok.*;
import java.math.BigDecimal;

import com.macbul.platform.util.City;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchListItemDto {
    private String id;
    private String fieldName;
    private City city;
    private String districtName;
    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;
    private Integer filledSlots;
    private boolean isUserJoined;
}
