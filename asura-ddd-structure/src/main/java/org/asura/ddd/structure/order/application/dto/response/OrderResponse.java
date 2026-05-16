package org.asura.ddd.structure.order.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class OrderResponse {

    private String id;
    private String userId;
    private List<OrderItemResponse> items;
    private ShippingAddressResponse shippingAddress;
    private String totalAmount;
    private String status;
    private String statusDescription;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderResponse() {
    }

    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.id = order.getId();
        response.userId = order.getUserId();
        response.items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        response.shippingAddress = order.getShippingAddress() != null ? ShippingAddressResponse.from(order.getShippingAddress()) : null;
        response.totalAmount = order.getTotalAmount().toPlainString();
        response.status = order.getStatus().name();
        response.statusDescription = order.getStatus().getDescription();
        response.createdAt = order.getCreatedAt() != null ? order.getCreatedAt().format(FORMATTER) : null;
        response.updatedAt = order.getUpdatedAt() != null ? order.getUpdatedAt().format(FORMATTER) : null;
        return response;
    }

}