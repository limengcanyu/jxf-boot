package org.asura.shardingsphere.mapper;

import org.asura.shardingsphere.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rock
 * @since 2022-06-15
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    void insertNew(User user);

    List<User> selectAll();

}
