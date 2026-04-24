package com.spring.boot.shardingsphere;

import com.spring.boot.shardingsphere.entity.Order;
import com.spring.boot.shardingsphere.entity.OrderItem;
import com.spring.boot.shardingsphere.entity.User;
import com.spring.boot.shardingsphere.mapper.OrderMapper;
import com.spring.boot.shardingsphere.service.IOrderItemService;
import com.spring.boot.shardingsphere.service.IOrderService;
import com.spring.boot.shardingsphere.service.IUserService;
import com.spring.boot.shardingsphere.utils.IDUtils;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class IOrderServiceTests {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void saveOrder() {
        for (int i = 1; i <= 10; i++) {
//            User user = new User();
//            user.setCreateTime(LocalDateTime.now());
//            iUserService.save(user);

            // 生成ID存在数据倾斜
            Long userId = IDUtils.generateKey();

            if (i % 2 == 0 && userId != null && userId % 2 == 0) {
                userId += 1;
            }

            Order order = new Order();
//            order.setUserId(userId);
//            order.setUserId(user.getUserId());
            order.setCreateTime(LocalDateTime.now());
            iOrderService.save(order);
            System.out.println("order: " + order);

            for (int j = 1; j <= 1; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getOrderId());
//                orderItem.setUserId(userId);
                orderItem.setCreateTime(LocalDateTime.now());
                iOrderItemService.save(orderItem);
                System.out.println("orderItem: " + orderItem);
            }
        }
    }

    @Test
    public void selectOrder() {
        List<Order> orderList = orderMapper.queryUserOrder(744154029319258112L);
        orderList.forEach(System.out::println);
    }

    @Test
    public void keyGenerate() {
        SnowflakeKeyGenerateAlgorithm algorithm = new SnowflakeKeyGenerateAlgorithm();
        for (int i = 0; i < 10; i++) {
            Comparable<?> comparable = algorithm.generateKey();
            if (comparable instanceof Long aLong) {
                System.out.println(aLong);
            }
        }
    }

}
