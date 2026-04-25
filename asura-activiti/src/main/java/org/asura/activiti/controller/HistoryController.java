package org.asura.activiti.controller;

import org.asura.activiti.service.IHistoryManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史查询控制器
 * 提供历史数据查询的REST API
 * 
 * @author Auto Generated
 * @version 1.0
 */
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private IHistoryManagementService historyManagementService;

    /**
     * 获取历史流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstances(
            @RequestParam(required = false) String processDefinitionKey) {
        
        List<HistoricProcessInstance> instances;
        if (processDefinitionKey != null && !processDefinitionKey.isEmpty()) {
            instances = historyManagementService.getHistoricProcessInstances(processDefinitionKey);
        } else {
            instances = historyManagementService.getFinishedProcessInstances();
        }
        return ResponseEntity.ok(instances);
    }

    /**
     * 根据流程实例ID获取历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例
     */
    @GetMapping("/process-instances/{processInstanceId}")
    public ResponseEntity<HistoricProcessInstance> getHistoricProcessInstance(
            @PathVariable String processInstanceId) {
        
        HistoricProcessInstance instance = historyManagementService.getHistoricProcessInstance(processInstanceId);
        if (instance != null) {
            return ResponseEntity.ok(instance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取已完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances/finished")
    public ResponseEntity<List<HistoricProcessInstance>> getFinishedProcessInstances() {
        List<HistoricProcessInstance> instances = historyManagementService.getFinishedProcessInstances();
        return ResponseEntity.ok(instances);
    }

    /**
     * 获取未完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances/unfinished")
    public ResponseEntity<List<HistoricProcessInstance>> getUnfinishedProcessInstances() {
        List<HistoricProcessInstance> instances = historyManagementService.getUnfinishedProcessInstances();
        return ResponseEntity.ok(instances);
    }

    /**
     * 根据启动用户获取历史流程实例
     * 
     * @param startedBy 启动用户
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances/starter/{startedBy}")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstancesByStarter(
            @PathVariable String startedBy) {
        
        List<HistoricProcessInstance> instances = historyManagementService.getHistoricProcessInstancesByStarter(startedBy);
        return ResponseEntity.ok(instances);
    }

    /**
     * 根据业务Key获取历史流程实例
     * 
     * @param businessKey 业务Key
     * @return 历史流程实例
     */
    @GetMapping("/process-instances/business/{businessKey}")
    public ResponseEntity<HistoricProcessInstance> getHistoricProcessInstanceByBusinessKey(
            @PathVariable String businessKey) {
        
        HistoricProcessInstance instance = historyManagementService.getHistoricProcessInstanceByBusinessKey(businessKey);
        if (instance != null) {
            return ResponseEntity.ok(instance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取历史任务实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances")
    public ResponseEntity<List<HistoricTaskInstance>> getHistoricTaskInstances(
            @RequestParam(required = false) String processInstanceId,
            @RequestParam(required = false) String assignee) {
        
        List<HistoricTaskInstance> tasks;
        if (processInstanceId != null && !processInstanceId.isEmpty()) {
            tasks = historyManagementService.getHistoricTaskInstances(processInstanceId);
        } else if (assignee != null && !assignee.isEmpty()) {
            tasks = historyManagementService.getHistoricTaskInstancesByAssignee(assignee);
        } else {
            tasks = historyManagementService.getFinishedHistoricTaskInstances();
        }
        return ResponseEntity.ok(tasks);
    }

    /**
     * 获取已完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances/finished")
    public ResponseEntity<List<HistoricTaskInstance>> getFinishedHistoricTaskInstances() {
        List<HistoricTaskInstance> tasks = historyManagementService.getFinishedHistoricTaskInstances();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 获取未完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances/unfinished")
    public ResponseEntity<List<HistoricTaskInstance>> getUnfinishedHistoricTaskInstances() {
        List<HistoricTaskInstance> tasks = historyManagementService.getUnfinishedHistoricTaskInstances();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 获取历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    @GetMapping("/activity-instances")
    public ResponseEntity<List<HistoricActivityInstance>> getHistoricActivityInstances(
            @RequestParam String processInstanceId) {
        
        List<HistoricActivityInstance> activities = historyManagementService.getHistoricActivityInstances(processInstanceId);
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取已完成的历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    @GetMapping("/activity-instances/{processInstanceId}/finished")
    public ResponseEntity<List<HistoricActivityInstance>> getFinishedHistoricActivityInstances(
            @PathVariable String processInstanceId) {
        
        List<HistoricActivityInstance> activities = historyManagementService.getFinishedHistoricActivityInstances(processInstanceId);
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取历史变量实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史变量实例列表
     */
    @GetMapping("/variable-instances")
    public ResponseEntity<List<HistoricVariableInstance>> getHistoricVariableInstances(
            @RequestParam String processInstanceId) {
        
        List<HistoricVariableInstance> variables = historyManagementService.getHistoricVariableInstances(processInstanceId);
        return ResponseEntity.ok(variables);
    }

    /**
     * 根据变量名获取历史变量实例
     * 
     * @param variableName 变量名
     * @return 历史变量实例列表
     */
    @GetMapping("/variable-instances/name/{variableName}")
    public ResponseEntity<List<HistoricVariableInstance>> getHistoricVariableInstancesByName(
            @PathVariable String variableName) {
        
        List<HistoricVariableInstance> variables = historyManagementService.getHistoricVariableInstancesByName(variableName);
        return ResponseEntity.ok(variables);
    }

    /**
     * 删除历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 操作结果
     */
    @DeleteMapping("/process-instances/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> deleteHistoricProcessInstance(
            @PathVariable String processInstanceId) {
        
        try {
            historyManagementService.deleteHistoricProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "历史流程实例删除成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "历史流程实例删除失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取流程统计信息
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 统计信息
     */
    @GetMapping("/statistics/{processDefinitionKey}")
    public ResponseEntity<Map<String, Object>> getProcessStatistics(
            @PathVariable String processDefinitionKey) {
        
        long totalCount = historyManagementService.getProcessInstanceCount(processDefinitionKey);
        long finishedCount = historyManagementService.getFinishedProcessInstanceCount(processDefinitionKey);
        long runningCount = totalCount - finishedCount;
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("processDefinitionKey", processDefinitionKey);
        statistics.put("totalCount", totalCount);
        statistics.put("finishedCount", finishedCount);
        statistics.put("runningCount", runningCount);
        statistics.put("completionRate", totalCount > 0 ? (double) finishedCount / totalCount * 100 : 0);
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取所有流程统计信息概览
     * 
     * @return 统计信息概览
     */
    @GetMapping("/statistics/overview")
    public ResponseEntity<Map<String, Object>> getProcessStatisticsOverview() {
        // 获取常见流程的统计信息
        String[] processKeys = {"leaveRequest", "purchaseRequest"};
        Map<String, Object> overview = new HashMap<>();
        
        for (String processKey : processKeys) {
            long totalCount = historyManagementService.getProcessInstanceCount(processKey);
            long finishedCount = historyManagementService.getFinishedProcessInstanceCount(processKey);
            
            Map<String, Object> processStats = new HashMap<>();
            processStats.put("totalCount", totalCount);
            processStats.put("finishedCount", finishedCount);
            processStats.put("runningCount", totalCount - finishedCount);
            
            overview.put(processKey, processStats);
        }
        
        return ResponseEntity.ok(overview);
    }
}