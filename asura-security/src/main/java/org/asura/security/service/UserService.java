package org.asura.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.asura.security.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    boolean registerUser(User user); // 包含密码加密
    List<String> getUserRoles(Long userId);
}
