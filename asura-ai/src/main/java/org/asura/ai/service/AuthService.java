package org.asura.ai.service;

import org.asura.ai.entity.User;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> login(String username, String password);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册的用户
     */
    User register(User user);

    /**
     * 获取当前登录用户
     * @return 当前用户
     */
    User getCurrentUser();

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(String oldPassword, String newPassword);

    /**
     * 退出登录（清除token，实际实现可配合Redis做token黑名单）
     * @param token 用户token
     */
    void logout(String token);
}