package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing a user’s profile. 
 * The primary key (userId) is shared with the User entity (one‐to‐one).
 */
@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    /**
     * Link back to the User entity. We map this PK to User.id via @MapsId.
     * FetchType.LAZY avoids loading the entire User unless specifically accessed.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 50)
    private String position;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
