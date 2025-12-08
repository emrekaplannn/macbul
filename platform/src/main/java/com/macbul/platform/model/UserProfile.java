package com.macbul.platform.model;

import com.macbul.platform.util.PlayerPosition;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    @Column(name = "avatar_path", length = 255)
    private String avatarPath;

    /**
     * Kullanıcının konumu artık districts tablosundaki id'ye bağlıdır.
     * (location → district_id FK)
     *
     * district_name NULL ise kullanıcı yalnızca şehir seçmiş demektir.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private District district;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
