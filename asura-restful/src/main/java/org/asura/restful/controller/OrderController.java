package org.asura.restful.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.asura.restful.dto.request.OrderCreateRequest;
import org.asura.restful.dto.response.ApiResponse;
import org.asura.restful.dto.response.OrderResponse;
import org.asura.restful.dto.response.PageResponse;
import org.asura.restful.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/orders")
@Tag(name = "订单管理", description = "订单CRUD操作")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "创建订单", description = "创建新订单")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        log.info("收到创建订单请求: userId={}, amount={}", request.getUserId(), request.getTotalAmount());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @Operation(summary = "获取订单详情", description = "根据ID获取订单详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @Parameter(description = "订单ID", required = true) @PathVariable String id) {
        log.info("收到查询订单请求: id={}", id);
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "获取订单列表", description = "分页获取订单列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> listOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") @Pattern(regexp = "^(asc|desc)$") String sortDir) {
        log.info("收到查询订单列表请求: page={}, size={}", page, size);
        PageResponse<OrderResponse> response = orderService.listOrders(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "更新订单状态", description = "更新订单状态")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @Parameter(description = "订单ID", required = true) @PathVariable String id,
            @Parameter(description = "订单状态", required = true) @RequestParam Integer status) {
        log.info("收到更新订单状态请求: id={}, status={}", id, status);
        OrderResponse response = orderService.updateOrder(id, status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "删除订单", description = "删除指定订单")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @Parameter(description = "订单ID", required = true) @PathVariable String id) {
        log.info("收到删除订单请求: id={}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}