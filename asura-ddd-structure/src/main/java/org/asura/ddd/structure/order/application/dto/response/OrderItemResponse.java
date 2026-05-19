package org.asura.ddd.structure.order.application.dto.response;

import org.asura.ddd.structure.order.domain.model.entity.OrderItem;

public class OrderItemResponse {

    private String productId;
    private String productName;
    private String unitPrice;
    private Integer quantity;
    private String subtotal;

    public OrderItemResponse() {
    }

    public static OrderItemResponse from(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.productId = item.getProductId();
        response.productName = item.getProductName();
        response.unitPrice = item.getUnitPrice().toPlainString();
        response.quantity = item.getQuantity();
        response.subtotal = item.getSubtotal().toPlainString();
        return response;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}