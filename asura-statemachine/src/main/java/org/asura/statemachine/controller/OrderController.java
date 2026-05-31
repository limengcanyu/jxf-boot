package org.asura.statemachine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.statemachine.domain.Order;
import org.asura.statemachine.enums.OrderEvent;
import org.asura.statemachine.service.OrderStateMachineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderStateMachineService orderStateMachineService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("创建订单请求: {}", order);
        Order createdOrder = orderStateMachineService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        log.info("查询订单请求，订单ID: {}", orderId);
        Order order = orderStateMachineService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/events")
    public ResponseEntity<Order> handleEvent(
            @PathVariable String orderId,
            @RequestParam String event) {
        log.info("处理订单事件请求，订单ID: {}, 事件: {}", orderId, event);
        OrderEvent orderEvent = OrderEvent.fromCode(event);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, orderEvent);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Order> payOrder(@PathVariable String orderId) {
        log.info("支付订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.PAY);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/ship")
    public ResponseEntity<Order> shipOrder(@PathVariable String orderId) {
        log.info("发货订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.SHIP);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrder(@PathVariable String orderId) {
        log.info("送达订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.DELIVER);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable String orderId) {
        log.info("完成订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.COMPLETE);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable String orderId) {
        log.info("取消订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.CANCEL);
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Order> refundOrder(@PathVariable String orderId) {
        log.info("退款订单请求，订单ID: {}", orderId);
        Order updatedOrder = orderStateMachineService.handleEvent(orderId, OrderEvent.REFUND);
        return ResponseEntity.ok(updatedOrder);
    }

}