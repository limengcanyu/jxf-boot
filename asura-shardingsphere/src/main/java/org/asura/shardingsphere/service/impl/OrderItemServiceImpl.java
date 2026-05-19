package org.asura.shardingsphere.service.impl;

import org.asura.shardingsphere.entity.OrderItem;
import org.asura.shardingsphere.mapper.OrderItemMapper;
import org.asura.shardingsphere.service.IOrderItemService;
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
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
