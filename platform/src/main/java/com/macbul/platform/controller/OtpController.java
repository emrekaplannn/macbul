// src/main/java/com/macbul/platform/controller/OtpController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.OtpService;
import com.macbul.platform.util.OtpType;
import com.macbul.platform.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "OTP API", description = "Create and verify one-time passwords (e.g. email verification)")
@RestController
@RequestMapping("/v1/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private SecurityUtils securityUtils;

    @Operation(summary = "Create new OTP for current user")
    @PostMapping
    public ResponseEntity<OtpCreateResponse> create(
            @Valid @RequestBody OtpCreateRequest req,
            @RequestParam(name = "returnCode", defaultValue = "false") boolean returnCode
    ) {
        String userId = securityUtils.getCurrentUserId();
        OtpCreateResponse out = otpService.create(userId, req, returnCode);
        return ResponseEntity.ok(out);
    }

    @Operation(summary = "Get latest active OTP metadata")
    @GetMapping("/active")
    public ResponseEntity<?> getActive(@RequestParam("type") OtpType type) {
        String userId = securityUtils.getCurrentUserId();
        Optional<OtpDto> otp = otpService.getActive(userId, type);
        return otp.<ResponseEntity<?>>map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Verify submitted OTP and consume")
    @PostMapping("/verify")
    public ResponseEntity<OtpVerifyResponse> verify(@Valid @RequestBody OtpVerifyRequest req) {
        String userId = securityUtils.getCurrentUserId();
        OtpVerifyResponse res = otpService.verifyAndConsume(userId, req);
        return ResponseEntity.ok(res);
    }
}
