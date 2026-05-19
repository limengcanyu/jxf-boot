package org.asura.ddd.structure.order.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.asura.ddd.structure.order.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {

    List<OrderItemEntity> selectByOrderId(@Param("orderId") String orderId);

    void deleteByOrderId(@Param("orderId") String orderId);
}