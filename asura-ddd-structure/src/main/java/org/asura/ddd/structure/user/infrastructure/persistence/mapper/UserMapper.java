package org.asura.ddd.structure.user.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.asura.ddd.structure.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

    IPage<UserEntity> selectPageByCondition(Page<UserEntity> page,
                                            @Param("username") String username,
                                            @Param("email") String email,
                                            @Param("enabled") Boolean enabled);

    UserEntity selectByUsername(@Param("username") String username);

    UserEntity selectByEmail(@Param("email") String email);
}