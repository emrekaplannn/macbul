// src/main/java/com/macbul/platform/controller/MatchResultController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.macbul.platform.service.MatchResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Match Result API")
@RestController @RequestMapping("/v1/match-results")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchResultService service;

    @Operation(summary="Get match result by matchId")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<MatchResultDto> getByMatch(@PathVariable String matchId){
        var dto = service.getByMatchId(matchId);
        return dto==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @Operation(summary="Upsert match result")
    @PostMapping
    public ResponseEntity<MatchResultDto> upsert(@RequestBody UpsertMatchResultRequest req){
        return ResponseEntity.ok(service.upsert(req));
    }

    @Operation(summary="Finalize a match (compute winner, set COMPLETED)")
    @PostMapping("/match/{matchId}/finalize")
    public ResponseEntity<MatchResultDto> finalizeMatch(@PathVariable String matchId){
        return ResponseEntity.ok(service.finalizeMatch(matchId));
    }
}
