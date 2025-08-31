package com.spring.boot.temporal;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OrderActivity {
    @ActivityMethod
    void initiateRefund(String orderId);

    @ActivityMethod
    String processPayment(String orderId, double amount);

    @ActivityMethod
    boolean validateOrder(String orderId);
}
