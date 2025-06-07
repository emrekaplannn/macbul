// src/main/java/com/macbul/platform/controller/VideoClipController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.VideoClipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "VideoClip API", description = "Create, read, update, delete, list video clips")
@RestController
@RequestMapping("/v1/video-clips")
public class VideoClipController {

    @Autowired private VideoClipService service;

    @Operation(summary = "Create a new video clip")
    @PostMapping
    public ResponseEntity<VideoClipDto> create(@RequestBody VideoClipCreateRequest req) {
        return ResponseEntity.ok(service.createClip(req));
    }

    @Operation(summary = "Get clip by ID")
    @GetMapping("/{id}")
    public ResponseEntity<VideoClipDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getClipById(id));
    }

    @Operation(summary = "List all clips")
    @GetMapping
    public ResponseEntity<List<VideoClipDto>> listAll() {
        return ResponseEntity.ok(service.getAllClips());
    }

    @Operation(summary = "List clips by match video ID")
    @GetMapping("/match-video/{matchVideoId}")
    public ResponseEntity<List<VideoClipDto>> listByMatchVideo(@PathVariable String matchVideoId) {
        return ResponseEntity.ok(service.getByMatchVideoId(matchVideoId));
    }

    @Operation(summary = "List clips by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoClipDto>> listByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @Operation(summary = "Update an existing video clip")
    @PutMapping("/{id}")
    public ResponseEntity<VideoClipDto> update(
        @PathVariable String id,
        @RequestBody VideoClipUpdateRequest req
    ) {
        return ResponseEntity.ok(service.updateClip(id, req));
    }

    @Operation(summary = "Delete a video clip")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteClip(id);
        return ResponseEntity.noContent().build();
    }
}
