package com.spring.boot.mybatisplus.sharding.jdbc.service;

import com.spring.boot.mybatisplus.sharding.jdbc.dao.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/25 0025
 */
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void select() {
        Order order = orderService.selectById(1);
        System.out.println("---------------------");
    }
}
