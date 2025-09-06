package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(length = 36)
    private String id;                // UUID

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Long registeredAt;        // epoch millis

    @Column
    private Integer overallScore;     // 0–100, NULL olabilir

    @Column(nullable = false)
    private Boolean isBanned = false;

    @Column(length = 20)
    private String referredByCode;
}
