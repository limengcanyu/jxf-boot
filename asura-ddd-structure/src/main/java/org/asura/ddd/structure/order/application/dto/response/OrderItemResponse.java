package org.asura.ddd.structure.order.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.asura.ddd.structure.order.domain.model.entity.OrderItem;

@Setter
@Getter
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

}