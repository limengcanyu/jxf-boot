package com.spring.boot.undertow.service.impl;

import com.spring.boot.undertow.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public String getOrderNumber() {
        return "order_9910";
    }

}
