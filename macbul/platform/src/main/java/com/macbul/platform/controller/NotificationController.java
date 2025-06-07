// src/main/java/com/macbul/platform/controller/NotificationController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification API", description = "Manage user notifications")
@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    @Autowired private NotificationService service;

    @Operation(summary = "Create a notification")
    @PostMapping
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationCreateRequest req) {
        return ResponseEntity.ok(service.createNotification(req));
    }

    @Operation(summary = "Get notification by ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List all notifications")
    @GetMapping
    public ResponseEntity<List<NotificationDto>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "List notifications by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> listByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @Operation(summary = "Mark notification as read/unread")
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> update(
        @PathVariable String id,
        @RequestBody NotificationUpdateRequest req
    ) {
        return ResponseEntity.ok(service.updateNotification(id, req));
    }

    @Operation(summary = "Delete notification")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
