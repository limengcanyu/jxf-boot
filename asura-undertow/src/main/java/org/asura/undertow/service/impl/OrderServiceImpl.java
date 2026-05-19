package org.asura.undertow.service.impl;

import org.asura.undertow.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public String getOrderNumber() {
        return "order_9910";
    }

}
