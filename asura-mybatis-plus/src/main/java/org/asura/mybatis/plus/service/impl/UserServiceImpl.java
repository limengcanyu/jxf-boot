package org.asura.mybatis.plus.service.impl;

import org.asura.mybatis.plus.entity.User;
import org.asura.mybatis.plus.mapper.UserMapper;
import org.asura.mybatis.plus.service.UserService;
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
