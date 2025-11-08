// src/main/java/com/macbul/dto/FeedbackResponse.java
package com.macbul.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FeedbackResponse", description = "Geri bildirim cevabı")
public record FeedbackResponse(
        @Schema(description = "Kayıt ID") Long id,
        @Schema(description = "Maç ID") String matchId,
        @Schema(description = "Veren kullanıcı ID") String reviewerId,
        @Schema(description = "Alan kullanıcı ID") String targetId,
        @Schema(description = "Puan (0–5)") int overallRating,
        @Schema(description = "Yorum") String comment,
        @Schema(description = "Oluşturulma zamanı (epoch ms)") Long createdAt,
        @Schema(description = "Güncellenme zamanı (epoch ms)") Long updatedAt
) {}
