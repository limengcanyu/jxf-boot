package org.asura.modulith.structure.order.listener;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.order.entity.Order;
import org.asura.modulith.structure.order.mapper.OrderMapper;
import org.asura.modulith.structure.shared.event.order.OrderCreatedEvent;
import org.asura.modulith.structure.shared.event.user.UserPlaceOrderEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleUserPlaceOrder(UserPlaceOrderEvent event) {
        System.out.println("Order 收到用户下单事件：" + event);

        Order order = new Order();
        order.setUserId(event.userId());
        order.setGoodsNum(event.goodsNum());
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        orderMapper.insert(order);

        // 向上游抛出订单完成事件，供库存/支付监听
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId(), "1001", 1));
        System.out.println("Order 订单创建事件已抛出");
    }

}
