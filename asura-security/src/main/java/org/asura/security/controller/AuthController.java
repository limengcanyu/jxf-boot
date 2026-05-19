package org.asura.security.controller;

import org.asura.security.entity.User;
import org.asura.security.service.UserService;
import org.asura.security.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            // 使用Spring Security进行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 认证成功后，将认证信息存入SecurityContext (虽然JWT Filter会再次设置，但这里也可以做)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成JWT Token
            String jwt = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("message", "登录成功");
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        // 检查用户名是否已存在等逻辑已在UserService中处理
        if (userService.registerUser(user)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户注册成功");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
