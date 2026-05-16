package org.asura.ddd.structure.order.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.asura.ddd.structure.common.dto.response.ApiResponse;
import org.asura.ddd.structure.order.application.dto.command.OrderCreateCommand;
import org.asura.ddd.structure.order.application.dto.command.OrderItemDTO;
import org.asura.ddd.structure.order.application.dto.command.OrderStatusCommand;
import org.asura.ddd.structure.order.application.dto.command.ShippingAddressDTO;
import org.asura.ddd.structure.order.application.dto.query.OrderPageQuery;
import org.asura.ddd.structure.order.application.dto.query.OrderQuery;
import org.asura.ddd.structure.order.application.dto.response.OrderResponse;
import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;
import org.asura.ddd.structure.order.domain.model.valueobject.ShippingAddress;
import org.asura.ddd.structure.order.domain.repository.OrderRepository;
import org.asura.ddd.structure.order.domain.service.OrderDomainService;
import org.asura.ddd.structure.order.infrastructure.repository.OrderRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderRepositoryImpl orderRepositoryImpl;

    public OrderApplicationService(OrderDomainService orderDomainService,
                                   OrderRepository orderRepository,
                                   OrderRepositoryImpl orderRepositoryImpl) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderRepositoryImpl = orderRepositoryImpl;
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

    public OrderResponse confirmOrder(OrderStatusCommand command) {
        Order order = orderDomainService.confirmOrder(command.getOrderId());
        return OrderResponse.from(order);
    }

    public OrderResponse payOrder(OrderStatusCommand command) {
        Order order = orderDomainService.payOrder(command.getOrderId());
        return OrderResponse.from(order);
    }

    public OrderResponse shipOrder(OrderStatusCommand command) {
        Order order = orderDomainService.shipOrder(command.getOrderId());
        return OrderResponse.from(order);
    }

    public OrderResponse completeOrder(OrderStatusCommand command) {
        Order order = orderDomainService.completeOrder(command.getOrderId());
        return OrderResponse.from(order);
    }

    public OrderResponse cancelOrder(OrderStatusCommand command) {
        Order order = orderDomainService.cancelOrder(command.getOrderId());
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

    public void delete(String orderId) {
        orderRepository.deleteById(orderId);
    }

    public ApiResponse<OrderResponse> queryPage(OrderPageQuery query) {
        IPage<Order> page = orderRepositoryImpl.findPage(
                query.getPageNum(), 
                query.getPageSize(), 
                query.getUserId(), 
                query.getStatus()
        );
        IPage<OrderResponse> responsePage = page.convert(OrderResponse::from);
        return ApiResponse.success(responsePage.getRecords(), responsePage);
    }

    public List<OrderResponse> queryList(OrderQuery query) {
        return orderRepositoryImpl.findList(query.getUserId(), query.getStatus())
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}