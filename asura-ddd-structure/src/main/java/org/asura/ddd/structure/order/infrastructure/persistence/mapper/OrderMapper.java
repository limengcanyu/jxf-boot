package org.asura.ddd.structure.order.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.asura.ddd.structure.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper extends BaseMapper<OrderEntity> {

    IPage<OrderEntity> selectPageByCondition(Page<OrderEntity> page,
                                             @Param("userId") String userId,
                                             @Param("status") String status);

    List<OrderEntity> selectByUserId(@Param("userId") String userId);

    List<OrderEntity> selectByUserIdAndStatus(@Param("userId") String userId,
                                               @Param("status") String status);
}