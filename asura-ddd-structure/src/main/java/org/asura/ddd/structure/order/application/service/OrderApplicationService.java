package org.asura.ddd.structure.order.application.service;

import org.asura.ddd.structure.order.application.dto.command.OrderCreateCommand;
import org.asura.ddd.structure.order.application.dto.command.OrderItemDTO;
import org.asura.ddd.structure.order.application.dto.command.ShippingAddressDTO;
import org.asura.ddd.structure.order.application.dto.response.OrderResponse;
import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;
import org.asura.ddd.structure.order.domain.service.OrderDomainService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private final OrderDomainService orderDomainService;

    public OrderApplicationService(OrderDomainService orderDomainService) {
        this.orderDomainService = orderDomainService;
    }

    public OrderResponse createOrder(OrderCreateCommand command) {
        ShippingAddress shippingAddress = ShippingAddress.create(
                command.getShippingAddress().getProvince(),
                command.getShippingAddress().getCity(),
                command.getShippingAddress().getDistrict(),
                command.getShippingAddress().getDetail(),
                command.getShippingAddress().getZipCode()
        );

        List<OrderItem> items = command.getItems().stream()
                .map(this::toOrderItem)
                .collect(Collectors.toList());

        Order order = orderDomainService.createOrder(command.getUserId(), shippingAddress, items);
        return OrderResponse.from(order);
    }

    private OrderItem toOrderItem(OrderItemDTO dto) {
        return OrderItem.create(
                dto.getProductId(),
                dto.getProductName(),
                dto.getUnitPrice(),
                dto.getQuantity()
        );
    }

    public OrderResponse confirmOrder(String orderId) {
        Order order = orderDomainService.confirmOrder(orderId);
        return OrderResponse.from(order);
    }

    public OrderResponse payOrder(String orderId) {
        Order order = orderDomainService.payOrder(orderId);
        return OrderResponse.from(order);
    }

    public OrderResponse shipOrder(String orderId) {
        Order order = orderDomainService.shipOrder(orderId);
        return OrderResponse.from(order);
    }

    public OrderResponse completeOrder(String orderId) {
        Order order = orderDomainService.completeOrder(orderId);
        return OrderResponse.from(order);
    }

    public OrderResponse cancelOrder(String orderId) {
        Order order = orderDomainService.cancelOrder(orderId);
        return OrderResponse.from(order);
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderDomainService.getOrderById(orderId);
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderDomainService.getOrdersByUserId(userId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}