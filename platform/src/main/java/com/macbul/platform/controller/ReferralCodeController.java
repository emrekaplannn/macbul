// src/main/java/com/macbul/platform/controller/ReferralCodeController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.ReferralCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ReferralCode API", description = "Manage referral codes")
@RestController
@RequestMapping("/v1/referral-codes")
public class ReferralCodeController {

    @Autowired private ReferralCodeService service;

    @Autowired
    private com.macbul.platform.util.SecurityUtils securityUtils;

    @Operation(summary = "Generate a new referral code")
    @PostMapping
    public ResponseEntity<ReferralCodeDto> create(@RequestBody ReferralCodeCreateRequest req) {
        String userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.create(req, userId));
    }

    @Operation(summary = "Get referral code by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReferralCodeDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/user-actives")
    public ReferralCodeDto getActiveForUser() {
        String userId = securityUtils.getCurrentUserId();
        return service.getActiveCodeForUser(userId);
    }

    @Operation(summary = "List all referral codes")
    @GetMapping
    public ResponseEntity<List<ReferralCodeDto>> listAll() {
        return ResponseEntity.ok(service.getAll());
        
    }

    @Operation(summary = "List by user ID")
    @GetMapping("/user")
    public ResponseEntity<List<ReferralCodeDto>> listByUser() {
        String userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @Operation(summary = "Get referral code by code value")
    @GetMapping("/code/{code}")
    public ResponseEntity<ReferralCodeDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @Operation(summary = "Update referral code value")
    @PutMapping("/{id}")
    public ResponseEntity<ReferralCodeDto> update(
        @PathVariable String id,
        @RequestBody ReferralCodeUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @Operation(summary = "Delete referral code")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
