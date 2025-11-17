// src/main/java/com/macbul/controller/MatchPlayerFeedbackController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchPlayerFeedbackService;
import com.macbul.platform.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/match-player-feedback")
@Tag(name = "Match Player Feedback", description = "Maç özelinde oyuncu geri bildirim uçları")
public class MatchPlayerFeedbackController {

    private final MatchPlayerFeedbackService service;

    @Autowired
    private SecurityUtils securityUtils;

    @Operation(summary = "Geri bildirim oluştur/güncelle (UPSERT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek")
    })
    @PostMapping
    public ResponseEntity<FeedbackResponse> upsert(@Valid @RequestBody FeedbackUpsertRequest req) {
        if(req.reviewerId() == null || req.reviewerId().isBlank()) {
            FeedbackUpsertRequest newReq = new FeedbackUpsertRequest(
                    req.matchId(),
                    securityUtils.getCurrentUserId(),
                    req.targetId(),
                    req.overallRating(),
                    req.comment()
            );
            return ResponseEntity.ok(service.upsert(newReq));

        }

        return ResponseEntity.ok(service.upsert(req));
    }

    @Operation(summary = "Maça göre tüm geri bildirimleri listele")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Liste döndü"))
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<FeedbackResponse>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.listByMatch(matchId));
    }

    @Operation(summary = "Kullanıcının aldığı geri bildirimleri listele")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Liste döndü"))
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<FeedbackResponse>> listReceived(@PathVariable String userId) {
        return ResponseEntity.ok(service.listReceivedByUser(userId));
    }

    @Operation(summary = "Bir maçta bir oyuncunun puan özetini getir")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Özet döndü"))
    @GetMapping("/summary/{matchId}/{targetId}")
    public ResponseEntity<PlayerMatchRatingSummary> summary(
            @PathVariable String matchId, @PathVariable String targetId) {
        return ResponseEntity.ok(service.getPlayerMatchSummary(matchId, targetId));
    }
}
