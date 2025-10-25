// src/main/java/com/macbul/platform/controller/MatchTeamResultController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchTeamResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name="Match Team Result API")
@RestController @RequestMapping("/v1/match-team-results")
@RequiredArgsConstructor
public class MatchTeamResultController {

    private final MatchTeamResultService service;

    @Operation(summary="List team scores by match")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchTeamResultDto>> list(@PathVariable String matchId){
        return ResponseEntity.ok(service.listByMatch(matchId));
    }

    @Operation(summary="Upsert a single team score")
    @PostMapping
    public ResponseEntity<MatchTeamResultDto> upsert(@RequestBody UpsertTeamScoreRequest req){
        return ResponseEntity.ok(service.upsertTeamScore(req));
    }

    @Operation(summary="Bulk upsert team scores (A & B)")
    @PostMapping("/bulk")
    public ResponseEntity<List<MatchTeamResultDto>> bulk(@RequestBody BulkUpsertTeamScoresRequest req){
        return ResponseEntity.ok(service.bulkUpsert(req));
    }
}
