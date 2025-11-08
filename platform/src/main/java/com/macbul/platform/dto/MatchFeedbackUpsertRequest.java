// src/main/java/com/macbul/dto/MatchFeedbackUpsertRequest.java
package com.macbul.platform.dto;

import com.macbul.platform.util.PaymentAppExperience;
import com.macbul.platform.util.FairPlay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "MatchFeedbackUpsertRequest", description = "Maç geri bildirimi (UPSERT)")
public record MatchFeedbackUpsertRequest(
        @Schema(description="Maç ID", example="mch-1234") @NotBlank String matchId,
        @Schema(description="Organizasyon kalitesi (0–5)", example="4") @Min(0) @Max(5) int organizationQuality,
        @Schema(description="Saha/Fasilite kalitesi (0–5)", example="5") @Min(0) @Max(5) int facilityQuality,
        @Schema(description="Fair Play", example="GOOD") @NotNull FairPlay fairPlay,
        @Schema(description="Ödeme & Uygulama Deneyimi", example="OK") @NotNull PaymentAppExperience paymentAppExperience,
        @Schema(description="Genel yorum (opsiyonel, max 255 karakter)") @Size(max=255) String comment
) {}