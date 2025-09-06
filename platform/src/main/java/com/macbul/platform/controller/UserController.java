package com.macbul.platform.controller;

import com.macbul.platform.dto.EmailVerificationRequest;
import com.macbul.platform.dto.UserCreateRequest;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.dto.UserUpdateRequest;
import com.macbul.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User API", description = "Endpoints for managing users (CRUDL)")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user with email, password, phone, and referral code"
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserCreateRequest request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.ok(created);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Returns the user corresponding to the provided UUID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        UserDto dto = userService.getUserById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "List all users",
        description = "Returns a list of all registered users"
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Operation(
        summary = "Update an existing user",
        description = "Updates the email, phone, or referral code for the specified user"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequest request) {
        UserDto updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }

    

    @Operation(
        summary = "Delete a user",
        description = "Deletes the user with the given UUID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // in UserController.java
    @Operation(
        summary = "Verify user email with code",
        description = "Marks the user's email as verified using an OTP code and userId"
    )
    @PostMapping("/verify-email")
    public ResponseEntity<UserDto> verifyEmailByCode(@RequestBody EmailVerificationRequest request) throws BadRequestException {
        UserDto updated = userService.verifyEmailByCode(request);
        return ResponseEntity.ok(updated);
    }

}
