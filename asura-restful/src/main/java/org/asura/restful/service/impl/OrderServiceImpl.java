package org.asura.restful.service.impl;

import org.asura.restful.dto.request.OrderCreateRequest;
import org.asura.restful.dto.response.OrderResponse;
import org.asura.restful.dto.response.PageResponse;
import org.asura.restful.entity.Order;
import org.asura.restful.exception.BusinessException;
import org.asura.restful.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final Map<String, Order> orderStorage = new ConcurrentHashMap<>();

    private static final Map<Integer, String> STATUS_DESC_MAP = new HashMap<>();
    static {
        STATUS_DESC_MAP.put(0, "待支付");
        STATUS_DESC_MAP.put(1, "已支付");
        STATUS_DESC_MAP.put(2, "已发货");
        STATUS_DESC_MAP.put(3, "已完成");
        STATUS_DESC_MAP.put(-1, "已取消");
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        log.info("创建订单: userId={}, amount={}", request.getUserId(), request.getTotalAmount());

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .orderNo(generateOrderNo())
                .totalAmount(request.getTotalAmount())
                .status(0)
                .remark(request.getRemark())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        orderStorage.put(order.getId(), order);
        log.info("订单创建成功: id={}, orderNo={}", order.getId(), order.getOrderNo());
        return toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderById(String id) {
        log.debug("查询订单: id={}", id);
        Order order = orderStorage.get(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }
        return toOrderResponse(order);
    }

    @Override
    public PageResponse<OrderResponse> listOrders(int page, int size, String sortBy, String sortDir) {
        log.debug("查询订单列表: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);

        List<Order> orders = new ArrayList<>(orderStorage.values());

        orders.sort((o1, o2) -> {
            int result = 0;
            if ("orderNo".equals(sortBy)) {
                result = o1.getOrderNo().compareTo(o2.getOrderNo());
            } else if ("status".equals(sortBy)) {
                result = Integer.compare(o1.getStatus(), o2.getStatus());
            } else if ("totalAmount".equals(sortBy)) {
                result = o1.getTotalAmount().compareTo(o2.getTotalAmount());
            } else {
                result = o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
            return "asc".equalsIgnoreCase(sortDir) ? result : -result;
        });

        long total = orders.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, orders.size());

        List<OrderResponse> responseList = orders.subList(Math.max(0, fromIndex), toIndex)
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return PageResponse.of(responseList, page, size, total);
    }

    @Override
    public OrderResponse updateOrder(String id, Integer status) {
        log.info("更新订单状态: id={}, status={}", id, status);
        Order order = orderStorage.get(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }

        if (order.getStatus() == 3 || order.getStatus() == -1) {
            throw BusinessException.badRequest("订单状态不允许修改");
        }

        if (!STATUS_DESC_MAP.containsKey(status)) {
            throw BusinessException.badRequest("无效的订单状态");
        }

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        log.info("订单状态更新成功: id={}, status={}", id, status);
        return toOrderResponse(order);
    }

    @Override
    public void deleteOrder(String id) {
        log.info("删除订单: id={}", id);
        if (!orderStorage.containsKey(id)) {
            throw BusinessException.notFound("订单不存在");
        }
        orderStorage.remove(id);
        log.info("订单删除成功: id={}", id);
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderNo(order.getOrderNo())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .statusDesc(STATUS_DESC_MAP.get(order.getStatus()))
                .remark(order.getRemark())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));
    }
}