package org.asura.statemachine.service;

import org.asura.statemachine.AsuraStatemachineApplication;
import org.asura.statemachine.domain.Order;
import org.asura.statemachine.enums.OrderEvent;
import org.asura.statemachine.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AsuraStatemachineApplication.class)
class OrderStateMachineServiceTest {

    @Autowired
    private OrderStateMachineService orderStateMachineService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setOrderNo("TEST_ORDER_001");
        testOrder.setCreatedBy("TEST_USER");
    }

    @Test
    @DisplayName("创建订单测试")
    void testCreateOrder() {
        Order created = orderStateMachineService.createOrder(testOrder);
        assertNotNull(created.getOrderId());
        assertEquals(OrderStatus.CREATED.getCode(), created.getStatus());
    }

    @Test
    @DisplayName("完整流程测试：创建 -> 支付 -> 发货 -> 送达 -> 完成")
    void testFullOrderFlow() {
        Order created = orderStateMachineService.createOrder(testOrder);
        String orderId = created.getOrderId();

        Order paid = orderStateMachineService.handleEvent(orderId, OrderEvent.PAY);
        assertEquals(OrderStatus.PAID.getCode(), paid.getStatus());

        Order shipped = orderStateMachineService.handleEvent(orderId, OrderEvent.SHIP);
        assertEquals(OrderStatus.SHIPPED.getCode(), shipped.getStatus());

        Order delivered = orderStateMachineService.handleEvent(orderId, OrderEvent.DELIVER);
        assertEquals(OrderStatus.DELIVERED.getCode(), delivered.getStatus());

        Order completed = orderStateMachineService.handleEvent(orderId, OrderEvent.COMPLETE);
        assertEquals(OrderStatus.COMPLETED.getCode(), completed.getStatus());
    }

    @Test
    @DisplayName("创建后取消订单测试")
    void testCancelOrderAfterCreate() {
        Order created = orderStateMachineService.createOrder(testOrder);
        String orderId = created.getOrderId();

        Order cancelled = orderStateMachineService.handleEvent(orderId, OrderEvent.CANCEL);
        assertEquals(OrderStatus.CANCELLED.getCode(), cancelled.getStatus());
    }

    @Test
    @DisplayName("支付后取消订单测试")
    void testCancelOrderAfterPaid() {
        Order created = orderStateMachineService.createOrder(testOrder);
        String orderId = created.getOrderId();

        orderStateMachineService.handleEvent(orderId, OrderEvent.PAY);
        Order cancelled = orderStateMachineService.handleEvent(orderId, OrderEvent.CANCEL);
        assertEquals(OrderStatus.CANCELLED.getCode(), cancelled.getStatus());
    }

    @Test
    @DisplayName("支付后退款测试")
    void testRefundAfterPaid() {
        Order created = orderStateMachineService.createOrder(testOrder);
        String orderId = created.getOrderId();

        orderStateMachineService.handleEvent(orderId, OrderEvent.PAY);
        Order refunded = orderStateMachineService.handleEvent(orderId, OrderEvent.REFUND);
        assertEquals(OrderStatus.REFUNDED.getCode(), refunded.getStatus());
    }

    @Test
    @DisplayName("无效状态转换测试")
    void testInvalidStateTransition() {
        Order created = orderStateMachineService.createOrder(testOrder);
        String orderId = created.getOrderId();

        assertThrows(IllegalStateException.class, () -> {
            orderStateMachineService.handleEvent(orderId, OrderEvent.SHIP);
        });
    }

    @Test
    @DisplayName("查询不存在的订单测试")
    void testGetNonExistentOrder() {
        Order order = orderStateMachineService.getOrderById("NON_EXISTENT_ID");
        assertNull(order);
    }

    @Test
    @DisplayName("不存在的订单执行事件测试")
    void testEventOnNonExistentOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderStateMachineService.handleEvent("NON_EXISTENT_ID", OrderEvent.PAY);
        });
    }

}