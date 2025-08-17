package com.macbul.platform.dto;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String email;
    private String phone;
    private Integer overallScore;
    private Boolean isBanned;
    private String referredByCode;
}
