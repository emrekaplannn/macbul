package com.macbul.platform.dto;

import lombok.Data;

@Data
public class CityResponse {
    private String city;
    private Integer districtId; // city-only district_id
}
