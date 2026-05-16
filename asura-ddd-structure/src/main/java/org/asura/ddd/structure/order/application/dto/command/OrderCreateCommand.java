package org.asura.ddd.structure.order.application.dto.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderCreateCommand {

    private String userId;
    private ShippingAddressDTO shippingAddress;
    private List<OrderItemDTO> items;

    public OrderCreateCommand() {
    }

    public OrderCreateCommand(String userId, ShippingAddressDTO shippingAddress, List<OrderItemDTO> items) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.items = items;
    }

}