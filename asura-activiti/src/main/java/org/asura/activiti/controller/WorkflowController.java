package org.asura.activiti.controller;

import org.asura.activiti.dto.ProcessDefinitionDTO;
import org.asura.activiti.service.IWorkflowService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流控制器
 * 提供流程管理的REST API
 * 
 * @author Auto Generated
 * @version 1.0
 */
@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    @Autowired
    private IWorkflowService workflowService;

    /**
     * 启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    @PostMapping("/start/{processDefinitionKey}")
    public ResponseEntity<Map<String, Object>> startProcess(
            @PathVariable String processDefinitionKey,
            @RequestBody Map<String, Object> variables) {
        
        try {
            String processInstanceId = workflowService.startProcess(processDefinitionKey, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("processInstanceId", processInstanceId);
            result.put("message", "流程启动成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程启动失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 根据业务Key启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    @PostMapping("/start/{processDefinitionKey}/business/{businessKey}")
    public ResponseEntity<Map<String, Object>> startProcessWithBusinessKey(
            @PathVariable String processDefinitionKey,
            @PathVariable String businessKey,
            @RequestBody Map<String, Object> variables) {
        
        try {
            String processInstanceId = workflowService.startProcessWithBusinessKey(
                processDefinitionKey, businessKey, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("processInstanceId", processInstanceId);
            result.put("businessKey", businessKey);
            result.put("message", "流程启动成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程启动失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 部署流程定义
     * 
     * @param processName 流程文件名
     * @return 部署结果
     */
    @PostMapping("/deploy")
    public ResponseEntity<Map<String, Object>> deployProcess(@RequestParam String processName) {
        try {
            String deploymentId = workflowService.deployProcess(processName);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("deploymentId", deploymentId);
            result.put("message", "流程部署成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程部署失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取所有流程定义
     * 
     * @return 流程定义列表
     */
    @GetMapping("/definitions")
    public ResponseEntity<List<ProcessDefinitionDTO>> getAllProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = workflowService.getAllProcessDefinitions();
        List<ProcessDefinitionDTO> dtoList = new ArrayList<>();
        
        for (ProcessDefinition pd : processDefinitions) {
            ProcessDefinitionDTO dto = new ProcessDefinitionDTO(
                pd.getId(),
                pd.getKey(),
                pd.getName(),
                pd.getVersion(),
                pd.getDeploymentId(),
                pd.getResourceName(),
                pd.isSuspended()
            );
            dtoList.add(dto);
        }
        
        return ResponseEntity.ok(dtoList);
    }

    /**
     * 根据Key获取最新版本的流程定义
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程定义
     */
    @GetMapping("/definitions/{processDefinitionKey}")
    public ResponseEntity<ProcessDefinition> getLatestProcessDefinition(
            @PathVariable String processDefinitionKey) {
        
        ProcessDefinition processDefinition = workflowService.getLatestProcessDefinition(processDefinitionKey);
        if (processDefinition != null) {
            return ResponseEntity.ok(processDefinition);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取正在运行的流程实例
     * 
     * @return 流程实例列表
     */
    @GetMapping("/instances/active")
    public ResponseEntity<List<ProcessInstance>> getActiveProcessInstances() {
        List<ProcessInstance> processInstances = workflowService.getActiveProcessInstances();
        return ResponseEntity.ok(processInstances);
    }

    /**
     * 根据流程定义Key获取运行中的流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例列表
     */
    @GetMapping("/instances/active/{processDefinitionKey}")
    public ResponseEntity<List<ProcessInstance>> getActiveProcessInstancesByKey(
            @PathVariable String processDefinitionKey) {
        
        List<ProcessInstance> processInstances = workflowService.getActiveProcessInstancesByKey(processDefinitionKey);
        return ResponseEntity.ok(processInstances);
    }

    /**
     * 根据业务Key获取流程实例
     * 
     * @param businessKey 业务Key
     * @return 流程实例
     */
    @GetMapping("/instances/business/{businessKey}")
    public ResponseEntity<ProcessInstance> getProcessInstanceByBusinessKey(
            @PathVariable String businessKey) {
        
        ProcessInstance processInstance = workflowService.getProcessInstanceByBusinessKey(businessKey);
        if (processInstance != null) {
            return ResponseEntity.ok(processInstance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 挂起流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 操作结果
     */
    @PutMapping("/instances/{processInstanceId}/suspend")
    public ResponseEntity<Map<String, Object>> suspendProcessInstance(
            @PathVariable String processInstanceId) {
        
        try {
            workflowService.suspendProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程实例挂起成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程实例挂起失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 激活流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 操作结果
     */
    @PutMapping("/instances/{processInstanceId}/activate")
    public ResponseEntity<Map<String, Object>> activateProcessInstance(
            @PathVariable String processInstanceId) {
        
        try {
            workflowService.activateProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程实例激活成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程实例激活失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 删除流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     * @return 操作结果
     */
    @DeleteMapping("/instances/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> deleteProcessInstance(
            @PathVariable String processInstanceId,
            @RequestParam(required = false, defaultValue = "手动删除") String deleteReason) {
        
        try {
            workflowService.deleteProcessInstance(processInstanceId, deleteReason);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程实例删除成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程实例删除失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程变量
     */
    @GetMapping("/instances/{processInstanceId}/variables")
    public ResponseEntity<Map<String, Object>> getProcessVariables(
            @PathVariable String processInstanceId) {
        
        Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
        return ResponseEntity.ok(variables);
    }

    /**
     * 设置流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variables 变量Map
     * @return 操作结果
     */
    @PutMapping("/instances/{processInstanceId}/variables")
    public ResponseEntity<Map<String, Object>> setProcessVariables(
            @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        
        try {
            workflowService.setProcessVariables(processInstanceId, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程变量设置成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "流程变量设置失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }
}