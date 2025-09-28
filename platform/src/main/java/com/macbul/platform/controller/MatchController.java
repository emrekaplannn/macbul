// src/main/java/com/macbul/platform/controller/MatchController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchParticipantService;
import com.macbul.platform.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Match API", description = "Create, read, update, delete, list matches")
@RestController
@RequestMapping("/v1/matches")
public class MatchController {

    @Autowired private MatchService matchService;

    @Autowired
    private MatchParticipantService matchParticipantService;

    @Operation(summary = "Create a new match")
    @PostMapping
    public ResponseEntity<MatchDto> create(@RequestBody MatchCreateRequest req) {
        return ResponseEntity.ok(matchService.createMatch(req));
    }

    @Operation(summary = "Get match by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @Operation(summary = "List all matches")
    @GetMapping
    public ResponseEntity<List<MatchDto>> listAll() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @Operation(summary = "List matches by organizer ID")
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<MatchDto>> listByOrganizer(@PathVariable String organizerId) {
        return ResponseEntity.ok(matchService.getMatchesByOrganizer(organizerId));
    }

    @Operation(summary = "Update match")
    @PutMapping("/{id}")
    public ResponseEntity<MatchDto> update(
        @PathVariable String id,
        @RequestBody MatchUpdateRequest req
    ) {
        return ResponseEntity.ok(matchService.updateMatch(id, req));
    }

    @Operation(summary = "Delete match")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get slots status (paid participants) for a match")
    @GetMapping("/{matchId}/slots")
    public ResponseEntity<MatchSlotsDto> getSlots(@PathVariable String matchId) {
        return ResponseEntity.ok(matchParticipantService.getSlotsStatus(matchId));
    }
}
