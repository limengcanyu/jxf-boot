package com.spring.boot.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.boot.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService; // 导入接口

import java.util.List;

// 继承 IService 并实现 UserDetailsService
public interface UserService extends IService<User>, UserDetailsService {
    User findByUsername(String username);
    boolean registerUser(User user); // 包含密码加密
    List<String> getUserRoles(Long userId);
    // loadUserByUsername 方法将由 UserDetailsService 接口定义
}
