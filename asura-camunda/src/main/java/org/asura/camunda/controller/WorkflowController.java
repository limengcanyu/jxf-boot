package org.asura.camunda.controller;

import org.asura.camunda.dto.*;
import org.asura.camunda.service.IWorkflowService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作流控制器
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class WorkflowController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired
    private IWorkflowService workflowService;

    // ========== 流程定义管理 ==========

    /**
     * 获取所有流程定义
     */
    @GetMapping("/definitions")
    public ApiResult<List<ProcessDefinitionDTO>> getAllProcessDefinitions() {
        try {
            logger.info("获取所有流程定义");
            List<ProcessDefinitionDTO> definitions = workflowService.getAllProcessDefinitions();
            return ApiResult.success("获取流程定义成功", definitions);
        } catch (Exception e) {
            logger.error("获取流程定义失败", e);
            return ApiResult.error("获取流程定义失败: " + e.getMessage());
        }
    }

    /**
     * 根据Key获取流程定义
     */
    @GetMapping("/definitions/{key}")
    public ApiResult<ProcessDefinitionDTO> getProcessDefinitionByKey(@PathVariable String key) {
        try {
            logger.info("根据Key获取流程定义: {}", key);
            ProcessDefinitionDTO definition = workflowService.getProcessDefinitionByKey(key);
            return ApiResult.success("获取流程定义成功", definition);
        } catch (Exception e) {
            logger.error("获取流程定义失败: {}", key, e);
            return ApiResult.error("获取流程定义失败: " + e.getMessage());
        }
    }

    /**
     * 挂起流程定义
     */
    @PutMapping("/definitions/{id}/suspend")
    public ApiResult<String> suspendProcessDefinition(@PathVariable String id) {
        try {
            logger.info("挂起流程定义: {}", id);
            workflowService.suspendProcessDefinition(id);
            return ApiResult.success("流程定义挂起成功");
        } catch (Exception e) {
            logger.error("挂起流程定义失败: {}", id, e);
            return ApiResult.error("挂起流程定义失败: " + e.getMessage());
        }
    }

    /**
     * 激活流程定义
     */
    @PutMapping("/definitions/{id}/activate")
    public ApiResult<String> activateProcessDefinition(@PathVariable String id) {
        try {
            logger.info("激活流程定义: {}", id);
            workflowService.activateProcessDefinition(id);
            return ApiResult.success("流程定义激活成功");
        } catch (Exception e) {
            logger.error("激活流程定义失败: {}", id, e);
            return ApiResult.error("激活流程定义失败: " + e.getMessage());
        }
    }

    // ========== 流程实例管理 ==========

    /**
     * 启动流程实例
     */
    @PostMapping("/instances/start")
    public ApiResult<ProcessInstanceDTO> startProcessInstance(@Valid @RequestBody StartProcessRequestDTO request) {
        try {
            logger.info("启动流程实例: {}", request);
            ProcessInstanceDTO instance = workflowService.startProcessInstance(
                    request.getProcessDefinitionKey(),
                    request.getBusinessKey(),
                    request.getVariables()
            );
            return ApiResult.success("流程实例启动成功", instance);
        } catch (Exception e) {
            logger.error("启动流程实例失败: {}", request, e);
            return ApiResult.error("启动流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 启动请假流程
     */
    @PostMapping("/instances/leave/start")
    public ApiResult<ProcessInstanceDTO> startLeaveProcess(@Valid @RequestBody LeaveRequestDTO leaveRequest) {
        try {
            logger.info("启动请假流程: {}", leaveRequest);
            ProcessInstanceDTO instance = workflowService.startLeaveProcess(leaveRequest);
            return ApiResult.success("请假流程启动成功", instance);
        } catch (Exception e) {
            logger.error("启动请假流程失败: {}", leaveRequest, e);
            return ApiResult.error("启动请假流程失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有流程实例
     */
    @GetMapping("/instances")
    public ApiResult<List<ProcessInstanceDTO>> getAllProcessInstances() {
        try {
            logger.info("获取所有流程实例");
            List<ProcessInstanceDTO> instances = workflowService.getAllProcessInstances();
            return ApiResult.success("获取流程实例成功", instances);
        } catch (Exception e) {
            logger.error("获取流程实例失败", e);
            return ApiResult.error("获取流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取流程实例
     */
    @GetMapping("/instances/{id}")
    public ApiResult<ProcessInstanceDTO> getProcessInstanceById(@PathVariable String id) {
        try {
            logger.info("根据ID获取流程实例: {}", id);
            ProcessInstanceDTO instance = workflowService.getProcessInstanceById(id);
            return ApiResult.success("获取流程实例成功", instance);
        } catch (Exception e) {
            logger.error("获取流程实例失败: {}", id, e);
            return ApiResult.error("获取流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 删除流程实例
     */
    @DeleteMapping("/instances/{id}")
    public ApiResult<String> deleteProcessInstance(@PathVariable String id, @RequestParam(required = false) String reason) {
        try {
            logger.info("删除流程实例: {}, 原因: {}", id, reason);
            workflowService.deleteProcessInstance(id, reason != null ? reason : "用户手动删除");
            return ApiResult.success("流程实例删除成功");
        } catch (Exception e) {
            logger.error("删除流程实例失败: {}", id, e);
            return ApiResult.error("删除流程实例失败: " + e.getMessage());
        }
    }

    // ========== 任务管理 ==========

    /**
     * 获取所有任务
     */
    @GetMapping("/tasks")
    public ApiResult<List<TaskDTO>> getAllTasks() {
        try {
            logger.info("获取所有任务");
            List<TaskDTO> tasks = workflowService.getAllTasks();
            return ApiResult.success("获取任务成功", tasks);
        } catch (Exception e) {
            logger.error("获取任务失败", e);
            return ApiResult.error("获取任务失败: " + e.getMessage());
        }
    }

    /**
     * 根据分配人获取任务
     */
    @GetMapping("/tasks/assignee/{assignee}")
    public ApiResult<List<TaskDTO>> getTasksByAssignee(@PathVariable String assignee) {
        try {
            logger.info("根据分配人获取任务: {}", assignee);
            List<TaskDTO> tasks = workflowService.getTasksByAssignee(assignee);
            return ApiResult.success("获取任务成功", tasks);
        } catch (Exception e) {
            logger.error("根据分配人获取任务失败: {}", assignee, e);
            return ApiResult.error("获取任务失败: " + e.getMessage());
        }
    }

    /**
     * 根据候选用户获取任务
     */
    @GetMapping("/tasks/candidate/{candidateUser}")
    public ApiResult<List<TaskDTO>> getTasksByCandidateUser(@PathVariable String candidateUser) {
        try {
            logger.info("根据候选用户获取任务: {}", candidateUser);
            List<TaskDTO> tasks = workflowService.getTasksByCandidateUser(candidateUser);
            return ApiResult.success("获取任务成功", tasks);
        } catch (Exception e) {
            logger.error("根据候选用户获取任务失败: {}", candidateUser, e);
            return ApiResult.error("获取任务失败: " + e.getMessage());
        }
    }

    /**
     * 根据流程实例ID获取任务
     */
    @GetMapping("/tasks/process/{processInstanceId}")
    public ApiResult<List<TaskDTO>> getTasksByProcessInstanceId(@PathVariable String processInstanceId) {
        try {
            logger.info("根据流程实例ID获取任务: {}", processInstanceId);
            List<TaskDTO> tasks = workflowService.getTasksByProcessInstanceId(processInstanceId);
            return ApiResult.success("获取任务成功", tasks);
        } catch (Exception e) {
            logger.error("根据流程实例ID获取任务失败: {}", processInstanceId, e);
            return ApiResult.error("获取任务失败: " + e.getMessage());
        }
    }

    /**
     * 完成任务
     */
    @PostMapping("/tasks/{taskId}/complete")
    public ApiResult<String> completeTask(@PathVariable String taskId, @RequestBody(required = false) CompleteTaskRequestDTO request) {
        try {
            logger.info("完成任务: {}", taskId);
            workflowService.completeTask(
                    taskId,
                    request != null ? request.getVariables() : null,
                    request != null ? request.getComment() : null
            );
            return ApiResult.success("任务完成成功");
        } catch (Exception e) {
            logger.error("完成任务失败: {}", taskId, e);
            return ApiResult.error("完成任务失败: " + e.getMessage());
        }
    }

    /**
     * 分配任务
     */
    @PutMapping("/tasks/{taskId}/assign/{assignee}")
    public ApiResult<String> assignTask(@PathVariable String taskId, @PathVariable String assignee) {
        try {
            logger.info("分配任务: taskId={}, assignee={}", taskId, assignee);
            workflowService.assignTask(taskId, assignee);
            return ApiResult.success("任务分配成功");
        } catch (Exception e) {
            logger.error("分配任务失败: taskId={}, assignee={}", taskId, assignee, e);
            return ApiResult.error("分配任务失败: " + e.getMessage());
        }
    }

    /**
     * 取消分配任务
     */
    @PutMapping("/tasks/{taskId}/unclaim")
    public ApiResult<String> unclaimTask(@PathVariable String taskId) {
        try {
            logger.info("取消分配任务: {}", taskId);
            workflowService.unclaimTask(taskId);
            return ApiResult.success("任务取消分配成功");
        } catch (Exception e) {
            logger.error("取消分配任务失败: {}", taskId, e);
            return ApiResult.error("取消分配任务失败: " + e.getMessage());
        }
    }

    // ========== 历史记录查询 ==========

    /**
     * 获取历史流程实例
     */
    @GetMapping("/history/instances")
    public ApiResult<List<ProcessInstanceDTO>> getHistoricProcessInstances() {
        try {
            logger.info("获取历史流程实例");
            List<ProcessInstanceDTO> instances = workflowService.getHistoricProcessInstances();
            return ApiResult.success("获取历史流程实例成功", instances);
        } catch (Exception e) {
            logger.error("获取历史流程实例失败", e);
            return ApiResult.error("获取历史流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 根据流程定义Key获取历史流程实例
     */
    @GetMapping("/history/instances/{processDefinitionKey}")
    public ApiResult<List<ProcessInstanceDTO>> getHistoricProcessInstancesByKey(@PathVariable String processDefinitionKey) {
        try {
            logger.info("根据流程定义Key获取历史流程实例: {}", processDefinitionKey);
            List<ProcessInstanceDTO> instances = workflowService.getHistoricProcessInstancesByKey(processDefinitionKey);
            return ApiResult.success("获取历史流程实例成功", instances);
        } catch (Exception e) {
            logger.error("根据流程定义Key获取历史流程实例失败: {}", processDefinitionKey, e);
            return ApiResult.error("获取历史流程实例失败: " + e.getMessage());
        }
    }

    /**
     * 获取历史任务
     */
    @GetMapping("/history/tasks")
    public ApiResult<List<TaskDTO>> getHistoricTasks() {
        try {
            logger.info("获取历史任务");
            List<TaskDTO> tasks = workflowService.getHistoricTasks();
            return ApiResult.success("获取历史任务成功", tasks);
        } catch (Exception e) {
            logger.error("获取历史任务失败", e);
            return ApiResult.error("获取历史任务失败: " + e.getMessage());
        }
    }

    /**
     * 根据流程实例ID获取历史任务
     */
    @GetMapping("/history/tasks/process/{processInstanceId}")
    public ApiResult<List<TaskDTO>> getHistoricTasksByProcessInstanceId(@PathVariable String processInstanceId) {
        try {
            logger.info("根据流程实例ID获取历史任务: {}", processInstanceId);
            List<TaskDTO> tasks = workflowService.getHistoricTasksByProcessInstanceId(processInstanceId);
            return ApiResult.success("获取历史任务成功", tasks);
        } catch (Exception e) {
            logger.error("根据流程实例ID获取历史任务失败: {}", processInstanceId, e);
            return ApiResult.error("获取历史任务失败: " + e.getMessage());
        }
    }
}