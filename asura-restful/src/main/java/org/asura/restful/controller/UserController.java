package org.asura.restful.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.asura.restful.dto.request.UserCreateRequest;
import org.asura.restful.dto.request.UserUpdateRequest;
import org.asura.restful.dto.response.ApiResponse;
import org.asura.restful.dto.response.PageResponse;
import org.asura.restful.dto.response.UserResponse;
import org.asura.restful.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "用户管理", description = "用户CRUD操作")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "创建用户", description = "创建新用户账户")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("收到创建用户请求: username={}", request.getUsername());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable String id) {
        log.info("收到查询用户请求: id={}", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> listUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") @Pattern(regexp = "^(asc|desc)$") String sortDir) {
        log.info("收到查询用户列表请求: page={}, size={}", page, size);
        PageResponse<UserResponse> response = userService.listUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @Parameter(description = "用户ID", required = true) @PathVariable String id,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("收到更新用户请求: id={}", id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "删除用户", description = "删除指定用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID", required = true) @PathVariable String id) {
        log.info("收到删除用户请求: id={}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}