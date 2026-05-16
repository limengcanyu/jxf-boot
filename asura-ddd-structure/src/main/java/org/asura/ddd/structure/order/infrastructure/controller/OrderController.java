package org.asura.ddd.structure.order.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.PageResponse;
import org.asura.ddd.structure.order.application.dto.command.OrderCreateCommand;
import org.asura.ddd.structure.order.application.dto.command.OrderStatusCommand;
import org.asura.ddd.structure.order.application.dto.query.OrderPageQuery;
import org.asura.ddd.structure.order.application.dto.query.OrderQuery;
import org.asura.ddd.structure.order.application.dto.response.OrderResponse;
import org.asura.ddd.structure.order.application.service.OrderApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderCreateCommand command) {
        OrderResponse response = orderApplicationService.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getByUserId(@PathVariable String userId) {
        List<OrderResponse> response = orderApplicationService.getOrdersByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/confirm")
    public ResponseEntity<OrderResponse> confirm(@RequestBody OrderStatusCommand command) {
        OrderResponse response = orderApplicationService.confirmOrder(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pay")
    public ResponseEntity<OrderResponse> pay(@RequestBody OrderStatusCommand command) {
        OrderResponse response = orderApplicationService.payOrder(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/ship")
    public ResponseEntity<OrderResponse> ship(@RequestBody OrderStatusCommand command) {
        OrderResponse response = orderApplicationService.shipOrder(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/complete")
    public ResponseEntity<OrderResponse> complete(@RequestBody OrderStatusCommand command) {
        OrderResponse response = orderApplicationService.completeOrder(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel")
    public ResponseEntity<OrderResponse> cancel(@RequestBody OrderStatusCommand command) {
        OrderResponse response = orderApplicationService.cancelOrder(command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable String orderId) {
        orderApplicationService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<OrderResponse>> queryPage(@ModelAttribute OrderPageQuery query) {
        PageResponse<OrderResponse> response = orderApplicationService.queryPage(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderResponse>> queryList(@ModelAttribute OrderQuery query) {
        List<OrderResponse> response = orderApplicationService.queryList(query);
        return ResponseEntity.ok(response);
    }
}