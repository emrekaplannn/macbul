// src/main/java/com/macbul/dto/FeedbackUpsertRequest.java
package com.macbul.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "FeedbackUpsertRequest", description = "Maç özelinde oyuncudan oyuncuya geri bildirim (UPSERT) isteği")
public record FeedbackUpsertRequest(
        @Schema(description = "Maç ID (UUID)", example = "mch-1234-...") @NotBlank String matchId,
        @Schema(description = "Geri bildirimi veren kullanıcı ID (UUID)", example = "usr-aaaa-...") String reviewerId,
        @Schema(description = "Geri bildirimi alan kullanıcı ID (UUID)", example = "usr-bbbb-...") @NotBlank String targetId,
        @Schema(description = "Toplam puan (0–5)", example = "4") @Min(0) @Max(5) int overallRating,
        @Schema(description = "Kısa yorum (opsiyonel, 255)", example = "Savunmada çok iyiydi") @Size(max=255) String comment
) {}
