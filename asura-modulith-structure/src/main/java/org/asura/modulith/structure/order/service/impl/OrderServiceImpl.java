package org.asura.modulith.structure.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.order.dto.CreateOrderDTO;
import org.asura.modulith.structure.order.mapper.OrderMapper;
import org.asura.modulith.structure.order.service.OrderService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Override
    public Long createOrder(CreateOrderDTO createOrderDTO) {
        System.out.println(createOrderDTO);
        return 0L;
    }

}
