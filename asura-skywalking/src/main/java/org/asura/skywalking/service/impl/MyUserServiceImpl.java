package org.asura.skywalking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;
import org.asura.skywalking.dto.request.UserCreateRequest;
import org.asura.skywalking.dto.request.UserQueryRequest;
import org.asura.skywalking.dto.request.UserUpdateRequest;
import org.asura.skywalking.dto.response.PageResponse;
import org.asura.skywalking.dto.response.UserResponse;
import org.asura.skywalking.exception.BusinessException;
import org.asura.skywalking.service.MyUserService;
import org.asura.skywalking.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class MyUserServiceImpl implements MyUserService {

    @Autowired
    private Executor asyncTaskExecutor;

    /**
     * 模拟数据库存储
     */
    private final Map<Long, User> userStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        log.info("创建用户: username={}, email={}", request.getUsername(), request.getEmail());

        // 检查邮箱是否已存在
        if (userStorage.values().stream()
                .anyMatch(user -> request.getEmail().equals(user.getEmail()))) {
            throw BusinessException.duplicate("邮箱", request.getEmail());
        }

        Long userId = idGenerator.getAndIncrement();
        User user = User.builder()
                .id(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userStorage.put(userId, user);
        log.info("用户创建成功: id={}", userId);

        return UserResponse.fromVo(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.info("查询用户: id={}", id);
        User user = userStorage.get(id);
        if (user == null) {
            throw BusinessException.notFound("用户", id);
        }
        return UserResponse.fromVo(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("更新用户: id={}", id);
        User user = userStorage.get(id);
        if (user == null) {
            throw BusinessException.notFound("用户", id);
        }

        // 更新非空字段
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            // 检查新邮箱是否与其他用户冲突
            if (userStorage.values().stream()
                    .anyMatch(u -> !u.getId().equals(id) && request.getEmail().equals(u.getEmail()))) {
                throw BusinessException.duplicate("邮箱", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        user.setUpdatedAt(LocalDateTime.now());

        userStorage.put(id, user);
        log.info("用户更新成功: id={}", id);

        return UserResponse.fromVo(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("删除用户: id={}", id);
        User user = userStorage.remove(id);
        if (user == null) {
            throw BusinessException.notFound("用户", id);
        }
        log.info("用户删除成功: id={}", id);
    }

    @Override
    public PageResponse<UserResponse> queryUsers(UserQueryRequest request) {
        log.info("分页查询用户: username={}, email={}, pageNum={}, pageSize={}",
                request.getUsername(), request.getEmail(), request.getPageNum(), request.getPageSize());

        List<User> allUsers = userStorage.values().stream()
                .filter(user -> {
                    if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                        if (!user.getUsername().contains(request.getUsername())) {
                            return false;
                        }
                    }
                    if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                        if (!user.getEmail().contains(request.getEmail())) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                .collect(Collectors.toList());

        long total = allUsers.size();
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allUsers.size());

        List<UserResponse> pageData = allUsers.subList(fromIndex, toIndex)
                .stream()
                .map(UserResponse::fromVo)
                .collect(Collectors.toList());

        return PageResponse.of(pageData, pageNum, pageSize, total);
    }

    @Override
    public List<UserResponse> asyncGetUser() {
        log.info("异步获取用户列表");

        // 异步有结果返回 - 使用SupplierWrapper进行SkyWalking追踪
        CompletableFuture<List<User>> future1 = CompletableFuture.supplyAsync(
                new SupplierWrapper<>(() -> {
                    log.debug("执行异步任务1");
                    return List.of(
                            User.builder().id(1L).username("asura").email("asura@example.com").build(),
                            User.builder().id(2L).username("alice").email("alice@example.com").build()
                    );
                }),
                asyncTaskExecutor
        );

        CompletableFuture<List<User>> future2 = CompletableFuture.supplyAsync(
                new SupplierWrapper<>(() -> {
                    log.debug("执行异步任务2");
                    return List.of(
                            User.builder().id(3L).username("artanis").email("artanis@example.com").build(),
                            User.builder().id(4L).username("bob").email("bob@example.com").build()
                    );
                }),
                asyncTaskExecutor
        );

        // 异步无结果返回 - 使用RunnableWrapper进行SkyWalking追踪
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(
                new RunnableWrapper(() -> {
                    log.debug("执行异步任务3（无返回结果）");
                    List<User> userList3 = List.of(User.builder().id(5L).username("akuma").build());
                    log.debug("userList3: {}", userList3);
                }),
                asyncTaskExecutor
        );

        // 等待所有任务完成
        CompletableFuture.allOf(future1, future2, future3).join();

        try {
            List<User> userList1 = new ArrayList<>(future1.get(6, TimeUnit.SECONDS));
            List<User> userList2 = future2.get(6, TimeUnit.SECONDS);

            log.debug("userList1: {}", userList1);
            log.debug("userList2: {}", userList2);

            userList1.addAll(userList2);

            return userList1.stream()
                    .map(UserResponse::fromVo)
                    .collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("异步任务执行失败", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("异步任务执行失败", e);
        }
    }

}