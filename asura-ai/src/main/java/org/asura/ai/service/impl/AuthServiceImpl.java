package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.entity.Role;
import org.asura.ai.entity.User;
import org.asura.ai.mapper.UserMapper;
import org.asura.ai.security.JwtTokenProvider;
import org.asura.ai.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    /** 存储已注销的token，用于退出登录 */
    private static final ConcurrentHashMap<String, Long> invalidatedTokens = new ConcurrentHashMap<>();

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenProvider tokenProvider;

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = tokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole().toString());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));

        logger.info("用户登录成功: {}", username);
        return result;
    }

    @Override
    @Transactional
    public User register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RuntimeException("邮箱不能为空");
        }

        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
        logger.info("用户注册成功: {}", user.getUsername());
        return user;
    }

    @Override
    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public boolean changePassword(String oldPassword, String newPassword) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.selectByUsername(username);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("新密码长度不能小于6位");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        logger.info("用户修改密码成功: {}", username);
        return true;
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token != null && !token.isEmpty()) {
            invalidatedTokens.put(token, System.currentTimeMillis());
            logger.info("用户退出登录，token已失效");
        }
    }

    /**
     * 检查token是否已注销
     */
    public static boolean isTokenInvalidated(String token) {
        return invalidatedTokens.containsKey(token);
    }
}