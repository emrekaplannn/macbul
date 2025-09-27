// src/main/java/com/macbul/platform/dto/otp/OtpCreateRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.OtpType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpCreateRequest {
    private OtpType type;   // EMAIL_VERIFY gibi
    private String destination; // email adresi gibi
}
