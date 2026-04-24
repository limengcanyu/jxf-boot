package com.spring.boot.shardingsphere.service.impl;

import com.spring.boot.shardingsphere.entity.User;
import com.spring.boot.shardingsphere.mapper.UserMapper;
import com.spring.boot.shardingsphere.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock
 * @since 2022-06-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
