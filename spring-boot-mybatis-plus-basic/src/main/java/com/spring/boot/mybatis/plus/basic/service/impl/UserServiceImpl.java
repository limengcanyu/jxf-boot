package com.spring.boot.mybatis.plus.basic.service.impl;

import com.spring.boot.mybatis.plus.basic.entity.User;
import com.spring.boot.mybatis.plus.basic.mapper.UserMapper;
import com.spring.boot.mybatis.plus.basic.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxf
 * @since 2025-08-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

}
