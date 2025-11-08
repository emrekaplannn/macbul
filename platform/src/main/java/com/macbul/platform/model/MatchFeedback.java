// src/main/java/com/macbul/model/MatchFeedback.java
package com.macbul.platform.model;

import jakarta.persistence.*;
import lombok.*;
import com.macbul.platform.util.FairPlay;
import com.macbul.platform.util.PaymentAppExperience;

@Entity
@Table(name = "match_feedback",
       uniqueConstraints = @UniqueConstraint(name = "ux_match_user", columnNames = {"match_id","user_id"}),
       indexes = {
           @Index(name="idx_match", columnList="match_id"),
           @Index(name="idx_user",  columnList="user_id")
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="match_id", nullable=false, length=36)
    private String matchId;

    @Column(name="user_id", nullable=false, length=36)
    private String userId; // feedback'i veren

    @Column(name="organization_quality", nullable=false)
    private short organizationQuality; // 0–5

    @Column(name="facility_quality", nullable=false)
    private short facilityQuality; // 0–5

    @Enumerated(EnumType.STRING)
    @Column(name="fair_play", nullable=false, length=16)
    private FairPlay fairPlay;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_app_experience", nullable=false, length=16)
    private PaymentAppExperience paymentAppExperience;

    @Column(name="comment", length=255)
    private String comment;

    @Column(name="created_at", nullable=false)
    private Long createdAt; // epoch ms

    @Column(name="updated_at")
    private Long updatedAt; // epoch ms

}
