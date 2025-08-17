// src/main/java/com/macbul/platform/dto/MatchCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields required to create a new Match.
 */
@Data
public class MatchCreateRequest {

    private String organizerId;
    private String fieldName;
    private String address;
    private String city;
    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;    // Optional: if null, service can default to 14
}
