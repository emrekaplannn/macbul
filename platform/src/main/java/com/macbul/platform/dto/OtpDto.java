// src/main/java/com/macbul/platform/dto/otp/OtpDto.java
package com.macbul.platform.dto;

import com.macbul.platform.model.Otp;
import com.macbul.platform.util.OtpType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpDto {
    private String id;
    private String userId;
    private String destination;
    private String code;
    private OtpType type;
    private Long createdAt;
    private Long expiresAt;
    private Long usedAt;

    public static OtpDto fromEntity(Otp otp) {
        return OtpDto.builder()
                .id(otp.getId())
                .userId(otp.getUserId())
                .destination(otp.getDestination())
                .code(otp.getCode())
                .type(otp.getType())
                .createdAt(otp.getCreatedAt())
                .expiresAt(otp.getExpiresAt())
                .usedAt(otp.getUsedAt())
                .build();
    }
}
