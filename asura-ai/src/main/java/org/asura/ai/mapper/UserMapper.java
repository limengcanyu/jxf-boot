package org.asura.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);

    User selectByEmail(String email);
}