package com.spring.boot.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow")
public class SignalQueryController {

    @Autowired
    private WorkflowClient workflowClient;

    // 发送 Signal：退款确认
    @PostMapping("/{workflowId}/signal/confirmRefund")
    public String confirmRefund(@PathVariable String workflowId, @RequestBody Map<String, Boolean> request) {
        WorkflowStub stub = workflowClient.newWorkflowStub(workflowId);
        stub.signal("confirmRefund", request.get("confirm"));
        return "已发送 confirmRefund=" + request.get("confirm");
    }

    // 发送 Signal：风控审批
    @PostMapping("/{workflowId}/signal/approveRisk")
    public String approveRisk(@PathVariable String workflowId, @RequestBody Map<String, Boolean> request) {
        WorkflowStub stub = workflowClient.newWorkflowStub(workflowId);
        stub.signal("approveRisk", request.get("approve"));
        return "已发送 approveRisk=" + request.get("approve");
    }

    // 查询状态
    @GetMapping("/{workflowId}/query/status")
    public String getStatus(@PathVariable String workflowId) {
        WorkflowStub stub = workflowClient.newWorkflowStub(workflowId);
        return stub.query(String.class, "status");
    }
}
