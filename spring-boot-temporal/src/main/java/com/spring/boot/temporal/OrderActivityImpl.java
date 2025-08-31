package com.spring.boot.temporal;

import org.springframework.beans.factory.annotation.Autowired;

public class OrderActivityImpl implements OrderActivity {

    @Autowired
    private OrderRepository orderRepository; // 假设是 Spring Bean 或手动注入

    @Override
    public void initiateRefund(String orderId) {
        System.out.println("🔄 正在处理退款，订单号: " + orderId);

        // 模拟调用第三方支付系统
        try {
            Thread.sleep(1000); // 模拟网络延迟
            // 调用支付宝/微信退款 API
            // refundService.callWeChatRefund(orderId);
        } catch (Exception e) {
            throw new RuntimeException("退款失败: " + e.getMessage(), e);
        }

        System.out.println("✅ 退款成功，订单号: " + orderId);
    }

    @Override
    public String processPayment(String orderId, double amount) {
        // 支付逻辑
        return "PAYMENT_SUCCESS";
    }

    @Override
    public boolean validateOrder(String orderId) {
        // 订单校验
        return true;
    }
}