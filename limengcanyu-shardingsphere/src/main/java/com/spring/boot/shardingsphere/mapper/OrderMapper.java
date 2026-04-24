package com.spring.boot.shardingsphere.mapper;

import com.spring.boot.shardingsphere.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
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
public interface OrderMapper extends BaseMapper<Order> {

    @Select("select o.order_id, o.create_time from t_order o left join t_order_item i on o.order_id = i.order_id")
    List<Order> queryOrderJoinOrderItem();

    @Select("select o.* from t_order o left join t_order_item i on o.order_id = i.order_id where o.user_id = #{userId}")
    List<Order> queryUserOrder(Long userId);
}
