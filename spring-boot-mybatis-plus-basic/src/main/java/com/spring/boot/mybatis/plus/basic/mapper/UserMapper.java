package com.spring.boot.mybatis.plus.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.boot.mybatis.plus.basic.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxf
 * @since 2025-08-30
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
