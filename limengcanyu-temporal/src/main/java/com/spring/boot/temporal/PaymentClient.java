package com.spring.boot.temporal;

import org.springframework.stereotype.Component;

@Component
public class PaymentClient {

    public boolean validate(String orderId) {
        if (Math.random() < 0.3) throw new RuntimeException("网络超时");
        return !orderId.contains("fail");
    }

    // 模拟大额订单风控
    public boolean riskCheck(String orderId) {
        System.out.println("🔍 执行风控审核: " + orderId);
        return !orderId.contains("riskfail"); // 可通过 signal 控制
    }
}
