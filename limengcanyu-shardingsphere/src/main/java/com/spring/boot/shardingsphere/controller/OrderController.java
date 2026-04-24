package com.spring.boot.shardingsphere.controller;

import com.spring.boot.shardingsphere.entity.Order;
import com.spring.boot.shardingsphere.entity.OrderItem;
import com.spring.boot.shardingsphere.mapper.OrderMapper;
import com.spring.boot.shardingsphere.service.IOrderItemService;
import com.spring.boot.shardingsphere.service.IOrderService;
import com.spring.boot.shardingsphere.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rock
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * <a href="http://localhost:8080/order/save">http://localhost:8080/order/save</a>
     *
     * @return
     */
    @RequestMapping("/save")
    public String save() {
        for (int i = 0; i < 10; i++) {
            Long userId = IDUtils.generateKey();

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

        return "ok";
    }

    /**
     * <a href="http://localhost:8080/order/query">http://localhost:8080/order/query</a>
     *
     * @return
     */
    @RequestMapping("/query")
    public String query() {
        List<Order> list = iOrderService.list();
        list.forEach(System.out::println);

        System.out.println("-----------------------------------");

        list = orderMapper.queryOrderJoinOrderItem();
        list.forEach(System.out::println);

        return "ok";
    }

}
