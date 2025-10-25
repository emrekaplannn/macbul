// src/main/java/com/macbul/platform/controller/MatchPlayerResultController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchPlayerResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name="Match Player Result API")
@RestController @RequestMapping("/v1/match-player-results")
@RequiredArgsConstructor
public class MatchPlayerResultController {

    private final MatchPlayerResultService service;

    @Operation(summary="List player results by match")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchPlayerResultDto>> listByMatch(@PathVariable String matchId){
        return ResponseEntity.ok(service.listByMatch(matchId));
    }

    @Operation(summary="Get a player's recent results")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MatchPlayerResultDto>> historyByUser(@PathVariable String userId){
        return ResponseEntity.ok(service.historyByUser(userId));
    }

    @Operation(summary="Upsert single player result")
    @PostMapping
    public ResponseEntity<MatchPlayerResultDto> upsert(@RequestBody UpsertPlayerResultRequest req){
        return ResponseEntity.ok(service.upsert(req));
    }

    @Operation(summary="Bulk upsert player results")
    @PostMapping("/bulk")
    public ResponseEntity<List<MatchPlayerResultDto>> bulk(@RequestBody BulkUpsertPlayerResultsRequest req){
        return ResponseEntity.ok(service.bulkUpsert(req));
    }
}
