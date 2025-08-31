package com.spring.boot.temporal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public void createOrder(String orderId, double amount) {
        OrderInfo info = new OrderInfo();
        info.setOrderId(orderId);
        info.setAmount(amount);
        info.setStatus("CREATED");
        orderInfoMapper.insert(info);
    }

    @Override
    public void updateStatus(String orderId, String status) {
        OrderInfo info = new OrderInfo();
        info.setOrderId(orderId);
        info.setStatus(status);
        orderInfoMapper.update(info, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<OrderInfo>()
                .eq("order_id", orderId));
    }

    @Override
    public void updateStatusWithReason(String orderId, String status, String reason) {
        OrderInfo info = new OrderInfo();
        info.setOrderId(orderId);
        info.setStatus(status);
        info.setReason(reason);
        orderInfoMapper.update(info, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<OrderInfo>()
                .eq("order_id", orderId));
    }

    @Override
    public OrderInfo getByOrderId(String orderId) {
        return orderInfoMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<OrderInfo>()
                .eq("order_id", orderId));
    }
}
