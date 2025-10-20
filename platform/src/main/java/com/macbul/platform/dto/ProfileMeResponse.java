package com.macbul.platform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileMeResponse {
    private String id;
    private String email;
    private Boolean emailVerified;
    private Boolean isBanned;
}
