package org.asura.ddd.structure.order.domain.model.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem {

    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    private OrderItem() {
    }

    public static OrderItem create(String productId, String productName, BigDecimal unitPrice, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        OrderItem item = new OrderItem();
        item.productId = productId;
        item.productName = productName;
        item.unitPrice = unitPrice;
        item.quantity = quantity;
        return item;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }

}