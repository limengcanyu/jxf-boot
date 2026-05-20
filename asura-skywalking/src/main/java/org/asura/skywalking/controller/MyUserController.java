package org.asura.skywalking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.asura.skywalking.dto.request.UserCreateRequest;
import org.asura.skywalking.dto.request.UserQueryRequest;
import org.asura.skywalking.dto.request.UserUpdateRequest;
import org.asura.skywalking.dto.response.CommonResponse;
import org.asura.skywalking.dto.response.PageResponse;
import org.asura.skywalking.dto.response.UserResponse;
import org.asura.skywalking.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class MyUserController {

    @Autowired
    private MyUserService myUserService;

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @return 用户响应
     */
    @PostMapping
    public ResponseEntity<CommonResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("接收到创建用户请求: {}", request);
        UserResponse response = myUserService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response));
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户响应
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(@PathVariable Long id) {
        log.info("接收到查询用户请求: id={}", id);
        UserResponse response = myUserService.getUserById(id);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 根据ID更新用户
     *
     * @param id      用户ID
     * @param request 更新用户请求
     * @return 用户响应
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("接收到更新用户请求: id={}, request={}", id, request);
        UserResponse response = myUserService.updateUser(id, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 空响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("接收到删除用户请求: id={}", id);
        myUserService.deleteUser(id);
        return ResponseEntity.ok(CommonResponse.success());
    }

    /**
     * 分页查询用户列表
     *
     * @param username 用户名（可选）
     * @param email    邮箱（可选）
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页响应
     */
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<UserResponse>>> queryUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("接收到分页查询用户请求: username={}, email={}, pageNum={}, pageSize={}",
                username, email, pageNum, pageSize);

        UserQueryRequest request = UserQueryRequest.builder()
                .username(username)
                .email(email)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        PageResponse<UserResponse> response = myUserService.queryUsers(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 异步获取用户列表（SkyWalking追踪示例）
     *
     * <a href="http://localhost:8081/api/users/async">...</a>
     *
     * @return 用户列表
     */
    @GetMapping("/async")
    public ResponseEntity<CommonResponse<List<UserResponse>>> asyncGetUser() {
        log.info("接收到异步获取用户请求");
        List<UserResponse> response = myUserService.asyncGetUser();
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}