// src/main/java/com/macbul/platform/controller/ReferralController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.ReferralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Referral API", description = "Manage referral records")
@RestController
@RequestMapping("/v1/referrals")
public class ReferralController {

    @Autowired private ReferralService service;

    @Operation(summary = "Create a new referral")
    @PostMapping
    public ResponseEntity<ReferralDto> create(@RequestBody ReferralCreateRequest req) {
        return ResponseEntity.ok(service.createReferral(req));
    }

    @Operation(summary = "Get referral by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReferralDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List all referrals")
    @GetMapping
    public ResponseEntity<List<ReferralDto>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "List by referrer user ID")
    @GetMapping("/referrer/{referrerUserId}")
    public ResponseEntity<List<ReferralDto>> listByReferrer(@PathVariable String referrerUserId) {
        return ResponseEntity.ok(service.getByReferrer(referrerUserId));
    }

    @Operation(summary = "List by referred user ID")
    @GetMapping("/referred/{referredUserId}")
    public ResponseEntity<List<ReferralDto>> listByReferred(@PathVariable String referredUserId) {
        return ResponseEntity.ok(service.getByReferred(referredUserId));
    }

    @Operation(summary = "List by match ID")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<ReferralDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.getByMatch(matchId));
    }

    @Operation(summary = "Update referral")
    @PutMapping("/{id}")
    public ResponseEntity<ReferralDto> update(
        @PathVariable String id,
        @RequestBody ReferralUpdateRequest req
    ) {
        return ResponseEntity.ok(service.updateReferral(id, req));
    }

    @Operation(summary = "Delete referral")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteReferral(id);
        return ResponseEntity.noContent().build();
    }
}
