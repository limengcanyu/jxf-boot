package com.spring.boot.mybatisplus.sharding.jdbc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spring.boot.mybatisplus.sharding.jdbc.dao.entity.Order;
import com.spring.boot.mybatisplus.sharding.jdbc.dao.mapper.OrderMapper;
import com.spring.boot.mybatisplus.sharding.jdbc.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-05-25
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
