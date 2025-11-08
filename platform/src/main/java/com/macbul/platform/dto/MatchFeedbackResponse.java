// src/main/java/com/macbul/dto/MatchFeedbackResponse.java
package com.macbul.platform.dto;

import com.macbul.platform.util.FairPlay;
import com.macbul.platform.util.PaymentAppExperience;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MatchFeedbackResponse", description = "Geri bildirim cevabı")
public record MatchFeedbackResponse(
        Long id,
        String matchId,
        String userId,
        int organizationQuality,
        int facilityQuality,
        FairPlay fairPlay,
        PaymentAppExperience paymentAppExperience,
        String comment,
        Long createdAt,
        Long updatedAt
) {}
