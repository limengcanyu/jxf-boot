package org.asura.modulith.structure.order.service;

import org.asura.modulith.structure.order.dto.CreateOrderDTO;

public interface OrderService {

    Long createOrder(CreateOrderDTO createOrderDTO);

}
