package com.spring.boot.mybatis.plus.service.impl;

import com.spring.boot.mybatis.plus.entity.User;
import com.spring.boot.mybatis.plus.mapper.UserMapper;
import com.spring.boot.mybatis.plus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock
 * @since 2022-12-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
