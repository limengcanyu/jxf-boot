package org.asura.restful.service;

import org.asura.restful.dto.request.OrderCreateRequest;
import org.asura.restful.dto.response.OrderResponse;
import org.asura.restful.dto.response.PageResponse;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest request);

    OrderResponse getOrderById(String id);

    PageResponse<OrderResponse> listOrders(int page, int size, String sortBy, String sortDir);

    OrderResponse updateOrder(String id, Integer status);

    void deleteOrder(String id);
}