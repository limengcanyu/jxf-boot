package org.asura.ddd.structure.order.application.dto.response;

import org.asura.ddd.structure.order.domain.model.aggregate.Order;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public ShippingAddressResponse getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressResponse shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}