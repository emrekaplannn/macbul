// src/main/java/com/macbul/platform/dto/otp/OtpCreateResponse.java
package com.macbul.platform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpCreateResponse {
    private OtpDto otp;
    private String code;  // test/dev için
}
