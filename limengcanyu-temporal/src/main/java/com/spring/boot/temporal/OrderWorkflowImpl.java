package com.spring.boot.temporal;

import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
// 实现类
public class OrderWorkflowImpl implements OrderWorkflow {

    // ✅ 使用 Workflow.newPromise() 创建 Promise
    private final Promise<Boolean> refundConfirmation = Workflow.newPromise();
    private final Promise<Boolean> riskApproval = Workflow.newPromise();

    private String status = "RUNNING";

    // 构造函数：不需要手动注册！
    public OrderWorkflowImpl() {
        // ❌ 删除旧代码
        // Workflow.setWorkflowMethodRunnable(...)
        // Workflow.registerQuery(...)
    }

    @Override
    public String processOrder(String orderId, double amount) {
        try {
            // 示例：高金额订单需要风控审批
            if (amount > 1000.0) {
                status = "WAITING_RISK_APPROVAL";

                // ✅ 等待信号（通过 Promise）
                Boolean approved = riskApproval.get(Duration.ofMinutes(30));
                if (!Boolean.TRUE.equals(approved)) {
                    status = "RISK_REJECTED";
                    return "FAILED";
                }
                status = "RISK_APPROVED";
            }

            // 正常流程...
            status = "PROCESSING";

            // 模拟异常，进入退款确认
            throw new RuntimeException("Payment failed");

        } catch (Exception e) {
            status = "WAITING_REFUND_CONFIRM";

            // ✅ 等待退款确认信号
            Boolean confirmed = refundConfirmation.get();
            if (Boolean.TRUE.equals(confirmed)) {
                activity.initiateRefund(orderId);
                status = "REFUND_INITIATED";
            } else {
                status = "REFUND_REJECTED";
            }

            return "FAILED";
        }
    }

    // ========== Signal Methods ==========
    // ✅ 使用 @SignalMethod 注解，SDK 自动注册
    @Override
    public void confirmRefund(boolean confirmed) {
        refundConfirmation.complete(confirmed);
    }

    @Override
    public void approveRisk(boolean approved) {
        riskApproval.complete(approved);
    }

    // ========== Query Method ==========
    // ✅ 使用 @QueryMethod 注解
    @Override
    public String getStatus() {
        return status;
    }
}