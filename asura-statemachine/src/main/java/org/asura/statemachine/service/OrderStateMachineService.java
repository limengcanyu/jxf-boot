package org.asura.statemachine.service;

import org.asura.statemachine.domain.Order;
import org.asura.statemachine.enums.OrderEvent;

public interface OrderStateMachineService {

    Order createOrder(Order order);

    Order handleEvent(String orderId, OrderEvent event);

    Order getOrderById(String orderId);

}