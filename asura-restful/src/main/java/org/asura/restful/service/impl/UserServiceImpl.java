package org.asura.restful.service.impl;

import org.asura.restful.dto.request.UserCreateRequest;
import org.asura.restful.dto.request.UserUpdateRequest;
import org.asura.restful.dto.response.PageResponse;
import org.asura.restful.dto.response.UserResponse;
import org.asura.restful.entity.User;
import org.asura.restful.exception.BusinessException;
import org.asura.restful.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Map<String, User> userStorage = new ConcurrentHashMap<>();

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        log.info("创建用户: username={}, email={}", request.getUsername(), request.getEmail());

        if (userStorage.values().stream().anyMatch(u -> u.getUsername().equals(request.getUsername()))) {
            throw BusinessException.badRequest("用户名已存在");
        }

        if (userStorage.values().stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw BusinessException.badRequest("邮箱已被注册");
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encryptPassword(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .status(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userStorage.put(user.getId(), user);
        log.info("用户创建成功: id={}", user.getId());
        return toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(String id) {
        log.debug("查询用户: id={}", id);
        User user = userStorage.get(id);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        return toUserResponse(user);
    }

    @Override
    public PageResponse<UserResponse> listUsers(int page, int size, String sortBy, String sortDir) {
        log.debug("查询用户列表: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);

        List<User> users = new ArrayList<>(userStorage.values());

        users.sort((u1, u2) -> {
            int result = 0;
            if ("username".equals(sortBy)) {
                result = u1.getUsername().compareTo(u2.getUsername());
            } else if ("email".equals(sortBy)) {
                result = u1.getEmail().compareTo(u2.getEmail());
            } else if ("status".equals(sortBy)) {
                result = Integer.compare(u1.getStatus(), u2.getStatus());
            } else {
                result = u2.getCreatedAt().compareTo(u1.getCreatedAt());
            }
            return "asc".equalsIgnoreCase(sortDir) ? result : -result;
        });

        long total = users.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, users.size());

        List<UserResponse> responseList = users.subList(Math.max(0, fromIndex), toIndex)
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());

        return PageResponse.of(responseList, page, size, total);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        log.info("更新用户: id={}", id);
        User user = userStorage.get(id);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userStorage.values().stream().anyMatch(u -> u.getUsername().equals(request.getUsername()) && !u.getId().equals(id))) {
                throw BusinessException.badRequest("用户名已存在");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userStorage.values().stream().anyMatch(u -> u.getEmail().equals(request.getEmail()) && !u.getId().equals(id))) {
                throw BusinessException.badRequest("邮箱已被注册");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getPassword() != null) {
            user.setPassword(encryptPassword(request.getPassword()));
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        user.setUpdatedAt(LocalDateTime.now());
        log.info("用户更新成功: id={}", id);
        return toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        log.info("删除用户: id={}", id);
        if (!userStorage.containsKey(id)) {
            throw BusinessException.notFound("用户不存在");
        }
        userStorage.remove(id);
        log.info("用户删除成功: id={}", id);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw BusinessException.internalError("密码加密失败");
        }
    }
}