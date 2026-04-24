package org.akuma.activiti.controller;

import org.akuma.activiti.dto.TaskResponseDTO;
import org.akuma.activiti.service.ITaskManagementService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务管理控制器
 * 提供任务相关的REST API
 * 
 * @author Auto Generated
 * @version 1.0
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ITaskManagementService taskManagementService;

    /**
     * 根据用户ID获取待办任务
     * 
     * @param assignee 用户ID
     * @return 任务列表
     */
    @GetMapping("/assignee/{assignee}")
    public ResponseEntity<List<Task>> getTasksByAssignee(@PathVariable String assignee) {
        List<Task> tasks = taskManagementService.getTasksByAssignee(assignee);
        return ResponseEntity.ok(tasks);
    }

    /**
     * 根据候选用户获取任务
     * 
     * @param candidateUser 候选用户
     * @return 任务列表
     */
    @GetMapping("/candidate-user/{candidateUser}")
    public ResponseEntity<List<Task>> getTasksByCandidateUser(@PathVariable String candidateUser) {
        List<Task> tasks = taskManagementService.getTasksByCandidateUser(candidateUser);
        return ResponseEntity.ok(tasks);
    }

    /**
     * 根据候选组获取任务
     * 
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    @GetMapping("/candidate-group/{candidateGroup}")
    public ResponseEntity<List<Task>> getTasksByCandidateGroup(@PathVariable String candidateGroup) {
        List<Task> tasks = taskManagementService.getTasksByCandidateGroup(candidateGroup);
        return ResponseEntity.ok(tasks);
    }

    /**
     * 根据流程实例ID获取任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    @GetMapping("/process/{processInstanceId}")
    public ResponseEntity<List<Task>> getTasksByProcessInstanceId(@PathVariable String processInstanceId) {
        List<Task> tasks = taskManagementService.getTasksByProcessInstanceId(processInstanceId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * 根据任务ID获取任务详情
     * 
     * @param taskId 任务ID
     * @return 任务对象
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String taskId) {
        Task task = taskManagementService.getTaskById(taskId);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有活动任务
     * 
     * @return 任务列表
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<Task> tasks = taskManagementService.getAllActiveTasks();
        List<TaskResponseDTO> dtoList = convertTasksToDTO(tasks);
        return ResponseEntity.ok(dtoList);
    }

    /**
     * 获取所有活动任务
     * 
     * @return 任务列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<TaskResponseDTO>> getAllActiveTasks() {
        List<Task> tasks = taskManagementService.getAllActiveTasks();
        List<TaskResponseDTO> dtoList = convertTasksToDTO(tasks);
        return ResponseEntity.ok(dtoList);
    }

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 任务变量
     * @return 操作结果
     */
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        
        try {
            if (variables != null && !variables.isEmpty()) {
                taskManagementService.completeTask(taskId, variables);
            } else {
                taskManagementService.completeTask(taskId);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务完成成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务完成失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/claim")
    public ResponseEntity<Map<String, Object>> claimTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        
        try {
            taskManagementService.claimTask(taskId, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务认领成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务认领失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 取消认领任务
     * 
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/unclaim")
    public ResponseEntity<Map<String, Object>> unclaimTask(@PathVariable String taskId) {
        try {
            taskManagementService.unclaimTask(taskId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "取消认领成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "取消认领失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param userId 被委派用户ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/delegate")
    public ResponseEntity<Map<String, Object>> delegateTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        
        try {
            taskManagementService.delegateTask(taskId, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务委派成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务委派失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 转派任务
     * 
     * @param taskId 任务ID
     * @param userId 新的处理人ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Map<String, Object>> assignTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        
        try {
            taskManagementService.assignTask(taskId, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务转派成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务转派失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取任务变量
     * 
     * @param taskId 任务ID
     * @return 任务变量
     */
    @GetMapping("/{taskId}/variables")
    public ResponseEntity<Map<String, Object>> getTaskVariables(@PathVariable String taskId) {
        Map<String, Object> variables = taskManagementService.getTaskVariables(taskId);
        return ResponseEntity.ok(variables);
    }

    /**
     * 设置任务变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     * @return 操作结果
     */
    @PutMapping("/{taskId}/variables")
    public ResponseEntity<Map<String, Object>> setTaskVariables(
            @PathVariable String taskId,
            @RequestBody Map<String, Object> variables) {
        
        try {
            taskManagementService.setTaskVariables(taskId, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务变量设置成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务变量设置失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取任务本地变量
     * 
     * @param taskId 任务ID
     * @return 任务本地变量
     */
    @GetMapping("/{taskId}/variables/local")
    public ResponseEntity<Map<String, Object>> getTaskVariablesLocal(@PathVariable String taskId) {
        Map<String, Object> variables = taskManagementService.getTaskVariablesLocal(taskId);
        return ResponseEntity.ok(variables);
    }

    /**
     * 设置任务本地变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     * @return 操作结果
     */
    @PutMapping("/{taskId}/variables/local")
    public ResponseEntity<Map<String, Object>> setTaskVariablesLocal(
            @PathVariable String taskId,
            @RequestBody Map<String, Object> variables) {
        
        try {
            taskManagementService.setTaskVariablesLocal(taskId, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务本地变量设置成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务本地变量设置失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 添加任务评论
     * 
     * @param taskId 任务ID
     * @param comment 评论内容
     * @return 操作结果
     */
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Map<String, Object>> addTaskComment(
            @PathVariable String taskId,
            @RequestBody Map<String, String> comment) {
        
        try {
            String message = comment.get("message");
            String processInstanceId = comment.get("processInstanceId");
            
            taskManagementService.addTaskComment(taskId, processInstanceId, message);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "评论添加成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "评论添加失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 设置任务优先级
     * 
     * @param taskId 任务ID
     * @param priority 优先级
     * @return 操作结果
     */
    @PutMapping("/{taskId}/priority")
    public ResponseEntity<Map<String, Object>> setTaskPriority(
            @PathVariable String taskId,
            @RequestParam int priority) {
        
        try {
            taskManagementService.setTaskPriority(taskId, priority);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "任务优先级设置成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "任务优先级设置失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 将Task列表转换为DTO列表
     * 
     * @param tasks 任务列表
     * @return DTO列表
     */
    private List<TaskResponseDTO> convertTasksToDTO(List<Task> tasks) {
        List<TaskResponseDTO> dtoList = new ArrayList<>();
        
        for (Task task : tasks) {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setTaskId(task.getId());
            dto.setTaskName(task.getName());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setProcessDefinitionId(task.getProcessDefinitionId());
            dto.setAssignee(task.getAssignee());
            dto.setOwner(task.getOwner());
            dto.setPriority(task.getPriority());
            dto.setDescription(task.getDescription());
            
            // 转换日期
            if (task.getCreateTime() != null) {
                dto.setCreateTime(task.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            if (task.getDueDate() != null) {
                dto.setDueDate(task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            
            // 获取任务变量（避免lazy loading问题）
            try {
                Map<String, Object> variables = taskManagementService.getTaskVariables(task.getId());
                dto.setVariables(variables);
            } catch (Exception e) {
                // 如果获取变量失败，设置为空集合
                dto.setVariables(new HashMap<>());
            }
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }
}