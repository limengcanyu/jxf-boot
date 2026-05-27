package org.asura.ai.controller;

import jakarta.annotation.Resource;
import org.asura.ai.entity.User;
import org.asura.ai.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供登录、注册、退出等认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 用户登录
     * POST /api/auth/login
     * 请求体: {"username": "用户名", "password": "密码"}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        return ResponseEntity.ok(authService.login(username, password));
    }

    /**
     * 用户注册
     * POST /api/auth/register
     * 请求体: {"username": "用户名", "password": "密码", "email": "邮箱"}
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "注册成功");
        result.put("user", Map.of(
                "id", registeredUser.getId(),
                "username", registeredUser.getUsername(),
                "email", registeredUser.getEmail(),
                "role", registeredUser.getRole()
        ));
        return ResponseEntity.ok(result);
    }

    /**
     * 获取当前登录用户信息
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    /**
     * 修改密码
     * PUT /api/auth/change-password
     * 请求体: {"oldPassword": "旧密码", "newPassword": "新密码"}
     */
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        authService.changePassword(oldPassword, newPassword);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "密码修改成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 用户退出登录
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        Map<String, Object> result = new HashMap<>();
        result.put("message", "退出成功");
        return ResponseEntity.ok(result);
    }
}