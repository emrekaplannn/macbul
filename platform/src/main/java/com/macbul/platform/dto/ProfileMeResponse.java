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
    private Integer overall;
    private String fullName;
    private String position;
    private String location;
    private String bio;

}
