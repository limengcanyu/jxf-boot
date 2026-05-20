package org.asura.skywalking.service;

import org.asura.skywalking.dto.request.UserCreateRequest;
import org.asura.skywalking.dto.request.UserQueryRequest;
import org.asura.skywalking.dto.request.UserUpdateRequest;
import org.asura.skywalking.dto.response.PageResponse;
import org.asura.skywalking.dto.response.UserResponse;

import java.util.List;

/**
 * 用户服务接口
 */
public interface MyUserService {

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @return 用户响应
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户响应
     */
    UserResponse getUserById(Long id);

    /**
     * 根据ID更新用户
     *
     * @param id      用户ID
     * @param request 更新用户请求
     * @return 用户响应
     */
    UserResponse updateUser(Long id, UserUpdateRequest request);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 分页查询用户列表
     *
     * @param request 查询请求
     * @return 分页响应
     */
    PageResponse<UserResponse> queryUsers(UserQueryRequest request);

    /**
     * 异步获取用户列表（SkyWalking追踪示例）
     *
     * @return 用户列表
     */
    List<UserResponse> asyncGetUser();

}