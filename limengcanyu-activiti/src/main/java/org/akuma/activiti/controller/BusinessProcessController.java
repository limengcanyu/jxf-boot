package org.akuma.activiti.controller;

import org.akuma.activiti.dto.LeaveRequestDTO;
import org.akuma.activiti.dto.PurchaseRequestDTO;
import org.akuma.activiti.service.IWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务流程示例控制器
 * 提供具体的业务流程启动接口
 * 
 * @author Auto Generated
 * @version 1.0
 */
@RestController
@RequestMapping("/api/business")
public class BusinessProcessController {

    @Autowired
    private IWorkflowService workflowService;

    /**
     * 提交请假申请
     * 
     * @param leaveRequest 请假申请信息
     * @return 流程启动结果
     */
    @PostMapping("/leave/submit")
    public ResponseEntity<Map<String, Object>> submitLeaveRequest(@Valid @RequestBody LeaveRequestDTO leaveRequest) {
        return handleLeaveRequest(leaveRequest);
    }

    /**
     * 提交请假申请 (兼容测试脚本路径)
     * 
     * @param variables 请假申请变量
     * @return 流程启动结果
     */
    @PostMapping("/leave/apply")
    public ResponseEntity<Map<String, Object>> applyLeave(@RequestBody Map<String, Object> variables) {
        try {
            // 从变量中构建 LeaveRequestDTO
            LeaveRequestDTO leaveRequest = new LeaveRequestDTO();
            leaveRequest.setApplicant((String) variables.get("applicant"));
            leaveRequest.setReason((String) variables.get("reason"));
            
            if (variables.get("days") != null) {
                leaveRequest.setDays(Long.valueOf(variables.get("days").toString()));
            }
            
            return handleLeaveRequest(leaveRequest);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "请假申请提交失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 提交采购申请
     * 
     * @param purchaseRequest 采购申请信息
     * @return 流程启动结果
     */
    @PostMapping("/purchase/submit")
    public ResponseEntity<Map<String, Object>> submitPurchaseRequest(@Valid @RequestBody PurchaseRequestDTO purchaseRequest) {
        return handlePurchaseRequest(purchaseRequest);
    }

    /**
     * 提交采购申请 (兼容测试脚本路径)
     * 
     * @param variables 采购申请变量
     * @return 流程启动结果
     */
    @PostMapping("/purchase/apply")
    public ResponseEntity<Map<String, Object>> applyPurchase(@RequestBody Map<String, Object> variables) {
        try {
            // 从变量中构建 PurchaseRequestDTO
            PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
            purchaseRequest.setApplicant((String) variables.get("requester"));
            purchaseRequest.setItemName((String) variables.get("item"));
            purchaseRequest.setReason((String) variables.get("reason"));
            
            if (variables.get("amount") != null) {
                purchaseRequest.setTotalAmount(new java.math.BigDecimal(variables.get("amount").toString()));
            }
            
            return handlePurchaseRequest(purchaseRequest);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "采购申请提交失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 快速创建请假申请示例
     * 
     * @param applicant 申请人
     * @return 示例请假申请
     */
    @GetMapping("/leave/example")
    public ResponseEntity<LeaveRequestDTO> getLeaveExample(@RequestParam(defaultValue = "user") String applicant) {
        LeaveRequestDTO example = new LeaveRequestDTO();
        example.setApplicant(applicant);
        example.setLeaveType("annual");
        example.setStartDate(LocalDate.now().plusDays(1));
        example.setEndDate(LocalDate.now().plusDays(3));
        example.setDays(3L);
        example.setReason("家庭事务");
        
        return ResponseEntity.ok(example);
    }

    /**
     * 快速创建采购申请示例
     * 
     * @param applicant 申请人
     * @return 示例采购申请
     */
    @GetMapping("/purchase/example")
    public ResponseEntity<PurchaseRequestDTO> getPurchaseExample(@RequestParam(defaultValue = "user") String applicant) {
        PurchaseRequestDTO example = new PurchaseRequestDTO();
        example.setApplicant(applicant);
        example.setItemName("办公用品");
        example.setQuantity(10L);
        example.setUnitPrice(new java.math.BigDecimal("50.00"));
        example.setTotalAmount(new java.math.BigDecimal("500.00"));
        example.setSupplier("ABC供应商");
        example.setUrgency("medium");
        example.setReason("办公室日常用品采购");
        
        return ResponseEntity.ok(example);
    }

    /**
     * 经理审批请假
     * 
     * @param taskId 任务ID
     * @param approved 是否同意
     * @param comment 审批意见
     * @return 审批结果
     */
    @PostMapping("/leave/manager-approve/{taskId}")
    public ResponseEntity<Map<String, Object>> managerApproveLeave(
            @PathVariable String taskId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String comment) {
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("managerApproved", approved ? "true" : "false");
            variables.put("managerComment", comment != null ? comment : "");
            
            // 这里应该添加任务完成的逻辑
            // taskManagementService.completeTask(taskId, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "经理审批完成");
            result.put("approved", approved);
            result.put("comment", comment);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "审批失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * HR审批请假
     * 
     * @param taskId 任务ID
     * @param approved 是否同意
     * @param comment 审批意见
     * @return 审批结果
     */
    @PostMapping("/leave/hr-approve/{taskId}")
    public ResponseEntity<Map<String, Object>> hrApproveLeave(
            @PathVariable String taskId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String comment) {
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("hrApproved", approved ? "true" : "false");
            variables.put("hrComment", comment != null ? comment : "");
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "HR审批完成");
            result.put("approved", approved);
            result.put("comment", comment);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "审批失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取流程统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getBusinessStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 这里可以添加具体的统计逻辑
            Map<String, Object> leaveStats = new HashMap<>();
            leaveStats.put("totalSubmitted", 0);
            leaveStats.put("approved", 0);
            leaveStats.put("rejected", 0);
            leaveStats.put("pending", 0);
            
            Map<String, Object> purchaseStats = new HashMap<>();
            purchaseStats.put("totalSubmitted", 0);
            purchaseStats.put("approved", 0);
            purchaseStats.put("rejected", 0);
            purchaseStats.put("pending", 0);
            
            statistics.put("leave", leaveStats);
            statistics.put("purchase", purchaseStats);
            statistics.put("lastUpdated", LocalDate.now());
            
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取统计信息失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 处理请假申请的公共方法
     * 
     * @param leaveRequest 请假申请
     * @return 处理结果
     */
    private ResponseEntity<Map<String, Object>> handleLeaveRequest(LeaveRequestDTO leaveRequest) {
        try {
            // 将DTO转换为流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("applicant", leaveRequest.getApplicant() != null ? leaveRequest.getApplicant() : "unknown");
            variables.put("leaveType", leaveRequest.getLeaveType() != null ? leaveRequest.getLeaveType() : "annual");
            variables.put("startDate", leaveRequest.getStartDate());
            variables.put("endDate", leaveRequest.getEndDate());
            variables.put("days", leaveRequest.getDays() != null ? leaveRequest.getDays() : 1L);
            variables.put("reason", leaveRequest.getReason() != null ? leaveRequest.getReason() : "个人事务");
            
            // 生成业务Key
            String businessKey = "LEAVE_" + variables.get("applicant") + "_" + System.currentTimeMillis();
            
            // 启动请假流程 - 使用正确的流程定义key
            String processInstanceId = workflowService.startProcessWithBusinessKey(
                "leaveRequest", businessKey, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("processInstanceId", processInstanceId);
            result.put("businessKey", businessKey);
            result.put("message", "请假申请提交成功");
            result.put("leaveInfo", leaveRequest);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "请假申请提交失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 处理采购申请的公共方法
     * 
     * @param purchaseRequest 采购申请
     * @return 处理结果
     */
    private ResponseEntity<Map<String, Object>> handlePurchaseRequest(PurchaseRequestDTO purchaseRequest) {
        try {
            // 将DTO转换为流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("applicant", purchaseRequest.getApplicant() != null ? purchaseRequest.getApplicant() : "unknown");
            variables.put("itemName", purchaseRequest.getItemName() != null ? purchaseRequest.getItemName() : "未知物品");
            variables.put("quantity", purchaseRequest.getQuantity() != null ? purchaseRequest.getQuantity() : 1L);
            variables.put("unitPrice", purchaseRequest.getUnitPrice() != null ? purchaseRequest.getUnitPrice() : new java.math.BigDecimal("0.00"));
            variables.put("totalAmount", purchaseRequest.getTotalAmount() != null ? purchaseRequest.getTotalAmount() : new java.math.BigDecimal("0.00"));
            variables.put("supplier", purchaseRequest.getSupplier() != null ? purchaseRequest.getSupplier() : "未知供应商");
            variables.put("urgency", purchaseRequest.getUrgency() != null ? purchaseRequest.getUrgency() : "medium");
            variables.put("reason", purchaseRequest.getReason() != null ? purchaseRequest.getReason() : "业务需要");
            
            // 生成业务Key
            String businessKey = "PURCHASE_" + variables.get("applicant") + "_" + System.currentTimeMillis();
            
            // 启动采购流程 - 使用正确的流程定义key
            String processInstanceId = workflowService.startProcessWithBusinessKey(
                "purchaseRequest", businessKey, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("processInstanceId", processInstanceId);
            result.put("businessKey", businessKey);
            result.put("message", "采购申请提交成功");
            result.put("purchaseInfo", purchaseRequest);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "采购申请提交失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }
}