package com.spring.boot.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.boot.security.entity.User;
import com.spring.boot.security.mapper.UserMapper;
import com.spring.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // 注入密码加密器

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 获取用户角色
            List<String> roles = userMapper.findRolesByUserId(user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    @Transactional // 保证注册过程的原子性
    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        if (this.findByUsername(user.getUsername()) != null) {
            return false; // 用户名已存在
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        boolean saved = this.save(user);

        // 默认分配 ROLE_USER 角色 (假设ROLE_USER的ID是1)
        // if (saved) {
        //     // 需要额外的Mapper方法来插入 user_roles 关联
        //     // userMapper.insertUserRole(user.getId(), 1L);
        // }
        return saved;
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        return userMapper.findRolesByUserId(userId);
    }

}