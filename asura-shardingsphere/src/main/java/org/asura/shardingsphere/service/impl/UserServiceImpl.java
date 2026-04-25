package org.asura.shardingsphere.service.impl;

import org.asura.shardingsphere.entity.User;
import org.asura.shardingsphere.mapper.UserMapper;
import org.asura.shardingsphere.service.IUserService;
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
