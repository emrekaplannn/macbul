// com.macbul.platform.model.OTP.java
package com.macbul.platform.model;

import com.macbul.platform.util.OtpType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "otp")
public class Otp {

    @Id
    @Column(length = 40)
    private String id;

    @Column(name = "user_id", length = 40, nullable = false)
    private String userId;

    @Column(name = "destination", length = 255, nullable = false)
    private String destination;

    @Column(name = "code", length = 64, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false)
    private OtpType type; // EMAIL_VERIFY

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "used_at")
    private Long usedAt;

    
}
