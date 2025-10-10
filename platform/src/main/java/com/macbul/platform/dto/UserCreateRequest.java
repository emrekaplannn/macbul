package com.macbul.platform.dto;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String email;
    private String password;
    private String phone;
    private String referredByCode;
    private Integer overallScore;
}
