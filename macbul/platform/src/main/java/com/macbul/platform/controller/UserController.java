package com.macbul.platform.controller;

import com.macbul.platform.dto.UserCreateRequest;
import com.macbul.platform.dto.UserDto;
import com.macbul.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "Operations for creating and retrieving users")
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
}
