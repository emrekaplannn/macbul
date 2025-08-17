// src/main/java/com/macbul/platform/dto/MatchDto.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO returned to clients for a Match.
 */
@Data
public class MatchDto {

    private String id;
    private String organizerId;
    private String fieldName;
    private String address;
    private String city;
    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;
    private Long createdAt;
}
