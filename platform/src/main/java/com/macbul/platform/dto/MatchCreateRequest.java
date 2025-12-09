// src/main/java/com/macbul/platform/dto/MatchCreateRequest.java
package com.macbul.platform.dto;

import lombok.Data;

import java.math.BigDecimal;

import com.macbul.platform.util.City;

/**
 * Fields required to create a new Match.
 */
@Data
public class MatchCreateRequest {
    private String organizerId;
    private String fieldName;
    private String address;

    private City city;           // "İstanbul", "Ankara", "İzmir"
    private String districtName;   // "Kadıköy", "Çankaya", "Karşıyaka" ...

    private Long matchTimestamp;
    private BigDecimal pricePerUser;
    private Integer totalSlots;
}

