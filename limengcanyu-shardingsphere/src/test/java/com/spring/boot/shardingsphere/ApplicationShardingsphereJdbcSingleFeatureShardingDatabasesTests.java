package com.spring.boot.shardingsphere;

import com.spring.boot.shardingsphere.entity.Order;
import com.spring.boot.shardingsphere.entity.OrderItem;
import com.spring.boot.shardingsphere.mapper.OrderMapper;
import com.spring.boot.shardingsphere.service.IOrderItemService;
import com.spring.boot.shardingsphere.service.IOrderService;
import com.spring.boot.shardingsphere.utils.IDUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ApplicationShardingsphereJdbcSingleFeatureShardingDatabasesTests {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void saveOrder() {
        // 存在数据倾斜问题
        for (int i = 1; i <= 10; i++) {
            Long userId = IDUtils.generateKey();

            // 解决数据倾斜问题
            if (i % 2 == 0 && userId != null && userId % 2 == 0) {
                userId += 1;
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setCreateTime(LocalDateTime.now());
            iOrderService.save(order);

            for (int j = 1; j <= 1; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getOrderId());
                orderItem.setUserId(userId);
                orderItem.setCreateTime(LocalDateTime.now());
                iOrderItemService.save(orderItem);
            }
        }
    }

    @Test
    public void selectOrder() {
        List<Order> list = iOrderService.list();
        list.forEach(System.out::println);

        System.out.println("-----------------------------------");

        list = orderMapper.queryOrderJoinOrderItem();
        list.forEach(System.out::println);
    }

}
