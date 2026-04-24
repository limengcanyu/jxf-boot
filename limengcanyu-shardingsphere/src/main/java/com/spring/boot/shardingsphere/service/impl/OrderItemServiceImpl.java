package com.spring.boot.shardingsphere.service.impl;

import com.spring.boot.shardingsphere.entity.OrderItem;
import com.spring.boot.shardingsphere.mapper.OrderItemMapper;
import com.spring.boot.shardingsphere.service.IOrderItemService;
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
