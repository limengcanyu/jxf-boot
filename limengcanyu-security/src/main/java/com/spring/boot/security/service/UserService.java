package com.spring.boot.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.boot.security.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    boolean registerUser(User user); // 包含密码加密
    List<String> getUserRoles(Long userId);
}
