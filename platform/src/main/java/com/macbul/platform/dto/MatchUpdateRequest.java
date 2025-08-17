// src/main/java/com/macbul/platform/dto/MatchUpdateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Fields that can be updated in an existing Match.
 * Only non-null values will be applied.
 */
@Data
public class MatchUpdateRequest {

    private String fieldName;
    private String address;
    private String city;
    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;
}
