package org.asura.ddd.structure.user.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.PageResponse;
import org.asura.ddd.structure.user.application.dto.command.UserRegisterCommand;
import org.asura.ddd.structure.user.application.dto.command.UserUpdateCommand;
import org.asura.ddd.structure.user.application.dto.query.UserPageQuery;
import org.asura.ddd.structure.user.application.dto.query.UserQuery;
import org.asura.ddd.structure.user.application.dto.response.UserResponse;
import org.asura.ddd.structure.user.application.service.UserApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterCommand command) {
        UserResponse response = userApplicationService.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable String userId) {
        UserResponse response = userApplicationService.getById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        UserResponse response = userApplicationService.getByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@PathVariable String userId, 
                                               @RequestBody UserUpdateCommand command) {
        UserResponse response = userApplicationService.updateProfile(userId, command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/disable")
    public ResponseEntity<Void> disable(@PathVariable String userId) {
        userApplicationService.disable(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/enable")
    public ResponseEntity<Void> enable(@PathVariable String userId) {
        userApplicationService.enable(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userApplicationService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<UserResponse>> queryPage(@ModelAttribute UserPageQuery query) {
        PageResponse<UserResponse> response = userApplicationService.queryPage(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> queryList(@ModelAttribute UserQuery query) {
        List<UserResponse> response = userApplicationService.queryList(query);
        return ResponseEntity.ok(response);
    }
}