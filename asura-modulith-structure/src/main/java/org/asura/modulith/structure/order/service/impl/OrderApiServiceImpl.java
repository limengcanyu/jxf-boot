package org.asura.modulith.structure.order.service.impl;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.order.api.OrderApiService;
import org.asura.modulith.structure.order.entity.Order;
import org.asura.modulith.structure.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApiServiceImpl implements OrderApiService {
    private final OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, Integer goodsNum) {
        Order order = new Order();
        order.setUserId(userId);
        order.setGoodsNum(goodsNum);
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        orderMapper.insert(order);
        return order.getId();
    }
}
