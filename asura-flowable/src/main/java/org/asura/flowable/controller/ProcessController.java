package org.asura.flowable.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.asura.flowable.dto.request.ProcessStartRequest;
import org.asura.flowable.dto.request.TaskCompleteRequest;
import org.asura.flowable.dto.request.TaskQueryRequest;
import org.asura.flowable.dto.response.*;
import org.asura.flowable.service.ProcessService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程控制器
 */
@RestController
@RequestMapping("/api/process")
@Validated
public class ProcessController {

    @Resource
    private ProcessService processService;

    /**
     * 启动流程实例
     */
    @PostMapping("/instances")
    public ApiResponse<ProcessInstanceDTO> startProcess(@Valid @RequestBody ProcessStartRequest request) {
        ProcessInstanceDTO result = processService.startProcess(request);
        return ApiResponse.success("流程启动成功", result);
    }

    /**
     * 查询流程实例详情
     */
    @GetMapping("/instances/{processInstanceId}")
    public ApiResponse<ProcessInstanceDTO> getProcessInstance(
            @NotBlank @PathVariable String processInstanceId) {
        ProcessInstanceDTO result = processService.getProcessInstance(processInstanceId);
        return ApiResponse.success(result);
    }

    /**
     * 挂起流程实例
     */
    @PutMapping("/instances/{processInstanceId}/suspend")
    public ApiResponse<Void> suspendProcess(@NotBlank @PathVariable String processInstanceId) {
        processService.suspendProcess(processInstanceId);
        return ApiResponse.success("流程已挂起", null);
    }

    /**
     * 激活流程实例
     */
    @PutMapping("/instances/{processInstanceId}/activate")
    public ApiResponse<Void> activateProcess(@NotBlank @PathVariable String processInstanceId) {
        processService.activateProcess(processInstanceId);
        return ApiResponse.success("流程已激活", null);
    }

    /**
     * 终止流程实例
     */
    @DeleteMapping("/instances/{processInstanceId}")
    public ApiResponse<Void> terminateProcess(
            @NotBlank @PathVariable String processInstanceId,
            @RequestParam(required = false) String reason) {
        processService.terminateProcess(processInstanceId, reason);
        return ApiResponse.success("流程已终止", null);
    }

    /**
     * 获取流程变量
     */
    @GetMapping("/instances/{processInstanceId}/variables")
    public ApiResponse<Map<String, Object>> getProcessVariables(
            @NotBlank @PathVariable String processInstanceId) {
        Map<String, Object> result = processService.getProcessVariables(processInstanceId);
        return ApiResponse.success(result);
    }

    /**
     * 设置流程变量
     */
    @PutMapping("/instances/{processInstanceId}/variables")
    public ApiResponse<Void> setProcessVariables(
            @NotBlank @PathVariable String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        processService.setProcessVariables(processInstanceId, variables);
        return ApiResponse.success("流程变量已更新", null);
    }

    /**
     * 查询待办任务列表
     */
    @PostMapping("/tasks/query")
    public ApiResponse<PageResponse<TaskDTO>> queryTasks(
            @RequestBody(required = false) TaskQueryRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        if (request == null) {
            request = new TaskQueryRequest();
        }
        PageResponse<TaskDTO> result = processService.queryTasks(request, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/tasks/{taskId}")
    public ApiResponse<TaskDTO> getTask(@NotBlank @PathVariable String taskId) {
        TaskDTO result = processService.getTask(taskId);
        return ApiResponse.success(result);
    }

    /**
     * 获取任务变量
     */
    @GetMapping("/tasks/{taskId}/variables")
    public ApiResponse<Map<String, Object>> getTaskVariables(@NotBlank @PathVariable String taskId) {
        Map<String, Object> result = processService.getTaskVariables(taskId);
        return ApiResponse.success(result);
    }

    /**
     * 签收任务
     */
    @PutMapping("/tasks/{taskId}/claim")
    public ApiResponse<Void> claimTask(
            @NotBlank @PathVariable String taskId,
            @RequestParam String assignee) {
        processService.claimTask(taskId, assignee);
        return ApiResponse.success("任务签收成功", null);
    }

    /**
     * 取消签收任务
     */
    @PutMapping("/tasks/{taskId}/unclaim")
    public ApiResponse<Void> unclaimTask(@NotBlank @PathVariable String taskId) {
        processService.unclaimTask(taskId);
        return ApiResponse.success("任务已取消签收", null);
    }

    /**
     * 完成任务
     */
    @PostMapping("/tasks/{taskId}/complete")
    public ApiResponse<Void> completeTask(
            @NotBlank @PathVariable String taskId,
            @Valid @RequestBody TaskCompleteRequest request) {
        request.setTaskId(taskId);
        processService.completeTask(request);
        return ApiResponse.success("任务处理成功", null);
    }

    /**
     * 委派任务
     */
    @PutMapping("/tasks/{taskId}/delegate")
    public ApiResponse<Void> delegateTask(
            @NotBlank @PathVariable String taskId,
            @RequestParam String assignee,
            @RequestParam(required = false) String comment) {
        processService.delegateTask(taskId, assignee, comment);
        return ApiResponse.success("任务委派成功", null);
    }

    /**
     * 转办任务
     */
    @PutMapping("/tasks/{taskId}/assign")
    public ApiResponse<Void> assignTask(
            @NotBlank @PathVariable String taskId,
            @RequestParam String assignee) {
        processService.assignTask(taskId, assignee);
        return ApiResponse.success("任务转办成功", null);
    }

    /**
     * 查询流程定义列表
     */
    @GetMapping("/definitions")
    public ApiResponse<PageResponse<ProcessDefinitionDTO>> queryProcessDefinitions(
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResponse<ProcessDefinitionDTO> result = processService.queryProcessDefinitions(
                processDefinitionKey, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 获取流程定义详情
     */
    @GetMapping("/definitions/{processDefinitionId}")
    public ApiResponse<ProcessDefinitionDTO> getProcessDefinition(
            @NotBlank @PathVariable String processDefinitionId) {
        ProcessDefinitionDTO result = processService.getProcessDefinition(processDefinitionId);
        return ApiResponse.success(result);
    }

    /**
     * 部署流程定义
     */
    @PostMapping("/definitions/deploy")
    public ApiResponse<String> deployProcess(
            @RequestParam String bpmnXml,
            @RequestParam String processName) {
        String result = processService.deployProcess(bpmnXml, processName);
        return ApiResponse.success("流程部署成功", result);
    }

    /**
     * 挂起流程定义
     */
    @PutMapping("/definitions/{processDefinitionId}/suspend")
    public ApiResponse<Void> suspendProcessDefinition(
            @NotBlank @PathVariable String processDefinitionId) {
        processService.suspendProcessDefinition(processDefinitionId);
        return ApiResponse.success("流程定义已挂起", null);
    }

    /**
     * 激活流程定义
     */
    @PutMapping("/definitions/{processDefinitionId}/activate")
    public ApiResponse<Void> activateProcessDefinition(
            @NotBlank @PathVariable String processDefinitionId) {
        processService.activateProcessDefinition(processDefinitionId);
        return ApiResponse.success("流程定义已激活", null);
    }

    /**
     * 获取流程历史任务列表
     */
    @GetMapping("/instances/{processInstanceId}/history/tasks")
    public ApiResponse<List<ProcessTaskHistoryDTO>> getProcessHistoryTasks(
            @NotBlank @PathVariable String processInstanceId) {
        List<ProcessTaskHistoryDTO> result = processService.getProcessHistoryTasks(processInstanceId);
        return ApiResponse.success(result);
    }

    /**
     * 获取流程图片
     */
    @GetMapping("/definitions/{processDefinitionId}/diagram")
    public ResponseEntity<byte[]> getProcessDiagram(
            @NotBlank @PathVariable String processDefinitionId) {
        byte[] diagram = processService.getProcessDiagram(processDefinitionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(diagram);
    }

    /**
     * 获取流程实例当前状态图片
     */
    @GetMapping("/instances/{processInstanceId}/diagram")
    public ResponseEntity<byte[]> getProcessInstanceDiagram(
            @NotBlank @PathVariable String processInstanceId) {
        byte[] diagram = processService.getProcessInstanceDiagram(processInstanceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(diagram);
    }
}