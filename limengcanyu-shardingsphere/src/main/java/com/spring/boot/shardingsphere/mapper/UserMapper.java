package com.spring.boot.shardingsphere.mapper;

import com.spring.boot.shardingsphere.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
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
