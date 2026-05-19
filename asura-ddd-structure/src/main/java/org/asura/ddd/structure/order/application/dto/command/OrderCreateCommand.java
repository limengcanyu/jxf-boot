package org.asura.ddd.structure.order.application.dto.command;

import java.util.List;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ShippingAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}