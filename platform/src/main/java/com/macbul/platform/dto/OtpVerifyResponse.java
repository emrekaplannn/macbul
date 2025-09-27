// src/main/java/com/macbul/platform/dto/otp/OtpVerifyResponse.java
package com.macbul.platform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerifyResponse {
    private boolean success;

    public static OtpVerifyResponse of(boolean success) {
        return OtpVerifyResponse.builder().success(success).build();
    }
}
