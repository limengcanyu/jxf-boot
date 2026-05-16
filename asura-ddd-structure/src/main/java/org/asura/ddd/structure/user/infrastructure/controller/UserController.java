package org.asura.ddd.structure.user.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.user.application.dto.command.UserRegisterCommand;
import org.asura.ddd.structure.user.application.dto.command.UserUpdateCommand;
import org.asura.ddd.structure.user.application.dto.response.UserResponse;
import org.asura.ddd.structure.user.application.service.UserApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRegisterCommand command) {
        UserResponse response = userApplicationService.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully", response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable String userId) {
        UserResponse response = userApplicationService.getById(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getByUsername(@PathVariable String username) {
        UserResponse response = userApplicationService.getByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestBody UserUpdateCommand command) {
        UserResponse response = userApplicationService.updateProfile(command);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @PatchMapping("/{userId}/disable")
    public ResponseEntity<ApiResponse<Void>> disable(@PathVariable String userId) {
        userApplicationService.disable(userId);
        return ResponseEntity.ok(ApiResponse.success("User disabled successfully", null));
    }

    @PatchMapping("/{userId}/enable")
    public ResponseEntity<ApiResponse<Void>> enable(@PathVariable String userId) {
        userApplicationService.enable(userId);
        return ResponseEntity.ok(ApiResponse.success("User enabled successfully", null));
    }
}