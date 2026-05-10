package org.asura.modulith.structure.order.controller;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.order.dto.CreateOrderDTO;
import org.asura.modulith.structure.order.service.OrderService;
import org.asura.modulith.structure.shared.result.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public R<Long> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return R.ok(orderService.createOrder(createOrderDTO));
    }

}
