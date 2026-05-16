package org.asura.ddd.structure.order.infrastructure.controller;

import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.order.application.dto.command.OrderCreateCommand;
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
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderCreateCommand command) {
        OrderResponse response = orderApplicationService.createOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderResponse> responses = orderApplicationService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.confirmOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order confirmed", response));
    }

    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<ApiResponse<OrderResponse>> payOrder(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.payOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order paid", response));
    }

    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<ApiResponse<OrderResponse>> shipOrder(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.shipOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order shipped", response));
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> completeOrder(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.completeOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order completed", response));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable String orderId) {
        OrderResponse response = orderApplicationService.cancelOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled", response));
    }
}