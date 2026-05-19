package org.asura.ddd.structure.order.application.dto.command;

public class OrderStatusCommand {

    private String orderId;

    public OrderStatusCommand() {
    }

    public OrderStatusCommand(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}