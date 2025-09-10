package com.macbul.platform.controller;

import com.macbul.platform.dto.auth.*;
import com.macbul.platform.model.User;
import com.macbul.platform.repository.UserRepository;
import com.macbul.platform.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository){
        this.authService = authService; this.userRepository = userRepository;
    }

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @Operation(summary = "Login with email & password")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest req){
        return ResponseEntity.ok(authService.refresh(req));
    }

    @Operation(summary = "Current user info (test)")
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal){
        User u = userRepository.findByEmail(principal.getUsername()).orElseThrow();
        record Me(String id, String email, Boolean emailVerified, Boolean isBanned){}
        return ResponseEntity.ok(new Me(u.getId(), u.getEmail(), u.getEmailVerified(), u.getIsBanned()));
    }
}
