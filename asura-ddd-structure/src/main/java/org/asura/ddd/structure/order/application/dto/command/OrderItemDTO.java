package org.asura.ddd.structure.order.application.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderItemDTO {

    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(String productId, String productName, BigDecimal unitPrice, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

}