package com.macbul.platform.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeResponse {
    private String id;
    private String email;
    private Boolean emailVerified;
    private Boolean isBanned;
}
