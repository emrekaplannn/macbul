// src/main/java/com/macbul/platform/dto/otp/OtpVerifyRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.OtpType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerifyRequest {
    private OtpType type;
    private String code;
}
