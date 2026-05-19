package org.asura.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private WorkflowClient workflowClient;

    @PostMapping
    public String createOrder(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.get("orderId");
        Double amount = request.containsKey("amount") ? ((Number) request.get("amount")).doubleValue() : 0.0;

        OrderWorkflow workflow = workflowClient.newWorkflowStub(OrderWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("order-task-queue")
                        .setWorkflowId("order-workflow-" + orderId)
                        .build());

        String result = WorkflowClient.execute(workflow::processOrder, orderId, amount);
        return "订单结果: " + result;
    }
}
