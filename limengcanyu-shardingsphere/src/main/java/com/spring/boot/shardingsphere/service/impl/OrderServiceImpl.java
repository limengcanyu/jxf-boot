package com.spring.boot.shardingsphere.service.impl;

import com.spring.boot.shardingsphere.entity.Order;
import com.spring.boot.shardingsphere.mapper.OrderMapper;
import com.spring.boot.shardingsphere.service.IOrderService;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
