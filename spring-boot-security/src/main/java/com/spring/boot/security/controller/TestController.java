package com.spring.boot.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')") // 需要 ROLE_USER 或 ROLE_ADMIN
    public ResponseEntity<?> userAccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "用户内容访问成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // 需要 ROLE_ADMIN
    public ResponseEntity<?> adminAccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "管理员内容访问成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    public ResponseEntity<?> publicAccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "这是公开内容");
        return ResponseEntity.ok(response);
    }
}
