// src/main/java/com/macbul/dto/PlayerMatchRatingSummary.java
package com.macbul.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PlayerMatchRatingSummary", description = "Bir maçta oyuncunun puan özeti")
public record PlayerMatchRatingSummary(
        @Schema(description = "Maç ID") String matchId,
        @Schema(description = "Hedef kullanıcı ID") String targetId,
        @Schema(description = "Ortalama puan") double averageRating,
        @Schema(description = "Toplam oy sayısı") long votes
) {}
