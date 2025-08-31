package com.spring.boot.mybatis.plus.basic.service;

import com.spring.boot.mybatis.plus.basic.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxf
 * @since 2025-08-30
 */
public interface UserService extends IService<User> {
    User getUserById(Long id);
}
