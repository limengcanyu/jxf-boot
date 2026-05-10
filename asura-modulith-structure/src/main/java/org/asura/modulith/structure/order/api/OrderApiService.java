package org.asura.modulith.structure.order.api;

public interface OrderApiService {
    Long createOrder(Long userId, Integer goodsNum);
}
