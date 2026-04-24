package com.spring.boot.temporal;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OrderWorkflow {
    @WorkflowMethod
    String processOrder(String orderId, double amount);

    @SignalMethod
    void confirmRefund(boolean confirmed);

    @SignalMethod
    void approveRisk(boolean approved);

    @QueryMethod
    String getStatus();
}
