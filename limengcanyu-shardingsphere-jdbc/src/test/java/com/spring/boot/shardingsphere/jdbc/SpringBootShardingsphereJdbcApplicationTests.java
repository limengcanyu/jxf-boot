package com.spring.boot.shardingsphere.jdbc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.boot.shardingsphere.jdbc.dao.entity.TOrder;
import com.spring.boot.shardingsphere.jdbc.dao.entity.TOrderItem;
import com.spring.boot.shardingsphere.jdbc.service.ITOrderItemService;
import com.spring.boot.shardingsphere.jdbc.service.ITOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class SpringBootShardingsphereJdbcApplicationTests {
    @Resource
    private DataSource dataSource;

    @Autowired
    private ITOrderService orderService;

    @Autowired
    private ITOrderItemService orderItemService;

    @Test
    void getDataSource() {
        System.out.println(dataSource.getClass());
    }

    @Test
    void save() {
        long start = System.currentTimeMillis();

        for (int i = 1; i <= 100; i++) {
            TOrder tOrder = new TOrder();
            tOrder.setUserId(i);
            tOrder.setOrderId(i);
            tOrder.setCreateTime(LocalDateTime.now());
            orderService.save(tOrder);

            TOrderItem tOrderItem = new TOrderItem();
            tOrderItem.setUserId(i);
            tOrderItem.setOrderId(i);
            tOrderItem.setOrderItemId(i);
            tOrderItem.setCreateTime(LocalDateTime.now());
            orderItemService.save(tOrderItem);
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start));
    }

    @Test
    void save2() {
        long start = System.currentTimeMillis();

        List<TOrder> tOrderList = new ArrayList<>();
        List<TOrderItem> tOrderItemList = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            System.out.println("====== i: " + i);

            TOrder tOrder = new TOrder();
            tOrder.setUserId(i);
            tOrder.setOrderId(i);
            tOrder.setCreateTime(LocalDateTime.now());
            tOrderList.add(tOrder);

            TOrderItem tOrderItem = new TOrderItem();
            tOrderItem.setUserId(i);
            tOrderItem.setOrderId(i);
            tOrderItem.setOrderItemId(i);
            tOrderItem.setCreateTime(LocalDateTime.now());
            tOrderItemList.add(tOrderItem);
        }
        orderService.saveBatch(tOrderList);
        orderItemService.saveBatch(tOrderItemList);

        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start));
    }

    @Test
    void getById() {
        TOrder tOrder = orderService.getById(2);
        System.out.println(tOrder);
    }

    @Test
    void list() {
        LambdaQueryWrapper<TOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(TOrder::getUserId, 20000);
        List<TOrder> tOrderList = orderService.list(queryWrapper);
        tOrderList.sort(Comparator.comparing(TOrder::getUserId));
        for (TOrder tOrder : tOrderList) {
            System.out.println(tOrder);
        }
    }

    @Test
    void tableRangeSave() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        TOrder tOrder = new TOrder();
        tOrder.setUserId(1);
        tOrder.setOrderId(1);
        tOrder.setCreateTime(LocalDateTime.now().minusMonths(1));
        orderService.save(tOrder);

        tOrder = new TOrder();
        tOrder.setUserId(2);
        tOrder.setOrderId(2);
        tOrder.setCreateTime(LocalDateTime.now().minusMonths(2));
        orderService.save(tOrder);

        tOrder = new TOrder();
        tOrder.setUserId(3);
        tOrder.setOrderId(3);
        tOrder.setCreateTime(LocalDateTime.now().minusMonths(4));
        orderService.save(tOrder);

        TOrderItem tOrderItem = new TOrderItem();
        tOrderItem.setUserId(1);
        tOrderItem.setOrderId(1);
        tOrderItem.setOrderItemId(1);
        tOrderItem.setCreateTime(LocalDateTime.now().minusMonths(1));
        orderItemService.save(tOrderItem);

        tOrderItem = new TOrderItem();
        tOrderItem.setUserId(2);
        tOrderItem.setOrderId(2);
        tOrderItem.setOrderItemId(2);
        tOrderItem.setCreateTime(LocalDateTime.now().minusMonths(3));
        orderItemService.save(tOrderItem);

        tOrderItem = new TOrderItem();
        tOrderItem.setUserId(3);
        tOrderItem.setOrderId(3);
        tOrderItem.setOrderItemId(3);
        tOrderItem.setCreateTime(LocalDateTime.now().minusMonths(4));
        orderItemService.save(tOrderItem);

    }

    @Test
    void tableRangeList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<TOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOrder::getCreateTime, LocalDateTime.parse("2020-04-01 00:00:00", formatter));

        List<TOrder> tOrderList = orderService.list(queryWrapper);
        tOrderList.sort(Comparator.comparing(TOrder::getUserId));
        for (TOrder tOrder : tOrderList) {
            System.out.println(tOrder);
        }
    }

    @Test
    void get() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<TOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(TOrder::getCreateTime, LocalDateTime.parse("2020-09-01 00:00:00", formatter));

        System.out.println(orderService.listMaps(queryWrapper));
    }

}
