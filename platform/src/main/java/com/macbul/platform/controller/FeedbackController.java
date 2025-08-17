// src/main/java/com/macbul/platform/controller/FeedbackController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Feedback API", description = "Manage user feedback on matches")
@RestController
@RequestMapping("/v1/feedback")
public class FeedbackController {

    @Autowired private FeedbackService feedbackService;

    @Operation(summary = "Submit feedback for a match")
    @PostMapping
    public ResponseEntity<FeedbackDto> create(@RequestBody FeedbackCreateRequest req) {
        return ResponseEntity.ok(feedbackService.createFeedback(req));
    }

    @Operation(summary = "Get feedback by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @Operation(summary = "List all feedback")
    @GetMapping
    public ResponseEntity<List<FeedbackDto>> listAll() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @Operation(summary = "List feedback for a match")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<FeedbackDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByMatchId(matchId));
    }

    @Operation(summary = "List feedback by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedbackDto>> listByUser(@PathVariable String userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByUserId(userId));
    }

    @Operation(summary = "Update feedback")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDto> update(
        @PathVariable String id,
        @RequestBody FeedbackUpdateRequest req
    ) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, req));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
