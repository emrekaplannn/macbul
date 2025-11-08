// src/main/java/com/macbul/controller/MatchFeedbackController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.MatchFeedbackResponse;
import com.macbul.platform.dto.MatchFeedbackUpsertRequest;
import com.macbul.platform.service.MatchFeedbackService;
import com.macbul.platform.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/match-feedback")
@Tag(name = "Match Feedback", description = "Maç geri bildirim uçları")
public class MatchFeedbackController {

    private final MatchFeedbackService service;
    private final SecurityUtils securityUtils; // mevcut user bilgisini almak için

    @Operation(summary = "Kullanıcının maç geri bildirimi oluştur/güncelle (UPSERT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek")
    })
    @PostMapping
    public ResponseEntity<MatchFeedbackResponse> upsert(@Valid @RequestBody MatchFeedbackUpsertRequest req) {
        final String userId = securityUtils.getCurrentUserId(); // 🔐 kullanıcı kimliği
        return ResponseEntity.ok(service.upsert(userId, req));
    }

    @Operation(summary = "Belirli maçtaki tüm geri bildirimleri getir")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Liste döndü"))
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchFeedbackResponse>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.listByMatch(matchId));
    }

    @Operation(summary = "Oturum açmış kullanıcının kendi geri bildirimlerini getir")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Liste döndü"))
    @GetMapping("/my-feedbacks")
    public ResponseEntity<List<MatchFeedbackResponse>> listMyFeedbacks() {
        final String userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.listByUser(userId));
    }
}
