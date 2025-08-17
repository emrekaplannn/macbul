// src/main/java/com/macbul/platform/controller/MatchVideoController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.MatchVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MatchVideo API", description = "Manage videos uploaded for matches")
@RestController
@RequestMapping("/v1/match-videos")
public class MatchVideoController {

    @Autowired private MatchVideoService service;

    @Operation(summary = "Upload a video for a match")
    @PostMapping
    public ResponseEntity<MatchVideoDto> create(@RequestBody MatchVideoCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @Operation(summary = "Get video by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MatchVideoDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List all videos")
    @GetMapping
    public ResponseEntity<List<MatchVideoDto>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "List videos by match ID")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchVideoDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.getByMatchId(matchId));
    }

    @Operation(summary = "Update video URL")
    @PutMapping("/{id}")
    public ResponseEntity<MatchVideoDto> update(
        @PathVariable String id,
        @RequestBody MatchVideoUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @Operation(summary = "Delete a video")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
