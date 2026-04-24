package com.spring.boot.temporal;

public interface OrderService {
    void createOrder(String orderId, double amount);
    void updateStatus(String orderId, String status);
    void updateStatusWithReason(String orderId, String status, String reason);
    OrderInfo getByOrderId(String orderId);
}
