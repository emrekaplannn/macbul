// src/main/java/com/macbul/platform/controller/MatchParticipantController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchParticipantService;
import com.macbul.platform.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MatchParticipant API", description = "Manage match participants")
@RestController
@RequestMapping("/v1/match-participants")
public class MatchParticipantController {

    @Autowired private MatchParticipantService service;
    @Autowired private SecurityUtils securityUtils;

    @Operation(summary = "Add participant to match")
    @PostMapping
    public ResponseEntity<MatchParticipantDto> create(
            @RequestBody MatchParticipantCreateRequest req
    ) {
        if(req.getUserId() == null){
            String userId = securityUtils.getCurrentUserId();
            req.setUserId(userId);

        }
        return ResponseEntity.ok(service.create(req));
    }

    @Operation(summary = "Get participant by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MatchParticipantDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List all participants")
    @GetMapping
    public ResponseEntity<List<MatchParticipantDto>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "List participants by match")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchParticipantDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.getByMatchId(matchId));
    }

    @Operation(summary = "List participants by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MatchParticipantDto>> listByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @Operation(summary = "Update participant")
    @PutMapping("/{id}")
    public ResponseEntity<MatchParticipantDto> update(
            @PathVariable String id,
            @RequestBody MatchParticipantUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @Operation(summary = "Remove participant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
