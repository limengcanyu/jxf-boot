package com.spring.boot.camunda.service.impl;

import com.spring.boot.camunda.dto.LeaveRequestDTO;
import com.spring.boot.camunda.dto.ProcessDefinitionDTO;
import com.spring.boot.camunda.dto.ProcessInstanceDTO;
import com.spring.boot.camunda.dto.TaskDTO;
import com.spring.boot.camunda.service.IWorkflowService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流服务实现类
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
@Service
public class WorkflowServiceImpl implements IWorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    // ========== 流程定义管理 ==========

    @Override
    public List<ProcessDefinitionDTO> getAllProcessDefinitions() {
        logger.info("获取所有流程定义");
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
        
        return definitions.stream()
                .map(this::convertToProcessDefinitionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProcessDefinitionDTO getProcessDefinitionByKey(String key) {
        logger.info("根据Key获取流程定义: {}", key);
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .latestVersion()
                .singleResult();
        
        if (definition == null) {
            throw new RuntimeException("未找到流程定义: " + key);
        }
        
        return convertToProcessDefinitionDTO(definition);
    }

    @Override
    public String deployProcessDefinition(String resourceName, String bpmnContent) {
        logger.info("部署流程定义: {}", resourceName);
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(resourceName, new ByteArrayInputStream(bpmnContent.getBytes()))
                .deploy();
        
        logger.info("流程定义部署成功，部署ID: {}", deployment.getId());
        return deployment.getId();
    }

    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        logger.info("挂起流程定义: {}", processDefinitionId);
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        logger.info("激活流程定义: {}", processDefinitionId);
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    // ========== 流程实例管理 ==========

    @Override
    public ProcessInstanceDTO startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        logger.info("启动流程实例: key={}, businessKey={}", processDefinitionKey, businessKey);
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                processDefinitionKey, businessKey, variables);
        
        logger.info("流程实例启动成功，实例ID: {}", processInstance.getId());
        return convertToProcessInstanceDTO(processInstance);
    }

    @Override
    public ProcessInstanceDTO startLeaveProcess(LeaveRequestDTO leaveRequest) {
        logger.info("启动请假流程: {}", leaveRequest);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", leaveRequest.getApplicant());
        variables.put("leaveType", leaveRequest.getLeaveType());
        variables.put("startDate", leaveRequest.getStartDate());
        variables.put("endDate", leaveRequest.getEndDate());
        variables.put("days", leaveRequest.getDays());
        variables.put("reason", leaveRequest.getReason());
        variables.put("phone", leaveRequest.getPhone());
        variables.put("urgency", leaveRequest.getUrgency());
        variables.put("approved", false);
        
        // 根据请假天数自动分配审批人
        if (leaveRequest.getDays() <= 3) {
            variables.put("approver", "manager");
        } else {
            variables.put("approver", "director");
        }
        
        String businessKey = "leave_" + leaveRequest.getApplicant() + "_" + System.currentTimeMillis();
        
        return startProcessInstance("leave-process", businessKey, variables);
    }

    @Override
    public List<ProcessInstanceDTO> getAllProcessInstances() {
        logger.info("获取所有流程实例");
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().list();
        
        return instances.stream()
                .map(this::convertToProcessInstanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProcessInstanceDTO getProcessInstanceById(String processInstanceId) {
        logger.info("根据ID获取流程实例: {}", processInstanceId);
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        if (instance == null) {
            throw new RuntimeException("未找到流程实例: " + processInstanceId);
        }
        
        return convertToProcessInstanceDTO(instance);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String reason) {
        logger.info("删除流程实例: {}, 原因: {}", processInstanceId, reason);
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    // ========== 任务管理 ==========

    @Override
    public List<TaskDTO> getAllTasks() {
        logger.info("获取所有任务");
        List<Task> tasks = taskService.createTaskQuery().list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByAssignee(String assignee) {
        logger.info("根据分配人获取任务: {}", assignee);
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByCandidateUser(String candidateUser) {
        logger.info("根据候选用户获取任务: {}", candidateUser);
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser(candidateUser)
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
        logger.info("根据流程实例ID获取任务: {}", processInstanceId);
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables, String comment) {
        logger.info("完成任务: {}", taskId);
        
        if (comment != null && !comment.trim().isEmpty()) {
            taskService.addComment(taskId, null, comment);
        }
        
        if (variables != null && !variables.isEmpty()) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
        
        logger.info("任务完成成功: {}", taskId);
    }

    @Override
    public void assignTask(String taskId, String assignee) {
        logger.info("分配任务: taskId={}, assignee={}", taskId, assignee);
        taskService.setAssignee(taskId, assignee);
    }

    @Override
    public void unclaimTask(String taskId) {
        logger.info("取消分配任务: {}", taskId);
        taskService.setAssignee(taskId, null);
    }

    // ========== 历史记录查询 ==========

    @Override
    public List<ProcessInstanceDTO> getHistoricProcessInstances() {
        logger.info("获取历史流程实例");
        List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime()
                .desc()
                .list();
        
        return instances.stream()
                .map(this::convertToProcessInstanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessInstanceDTO> getHistoricProcessInstancesByKey(String processDefinitionKey) {
        logger.info("根据流程定义Key获取历史流程实例: {}", processDefinitionKey);
        List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .orderByProcessInstanceStartTime()
                .desc()
                .list();
        
        return instances.stream()
                .map(this::convertToProcessInstanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getHistoricTasks() {
        logger.info("获取历史任务");
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getHistoricTasksByProcessInstanceId(String processInstanceId) {
        logger.info("根据流程实例ID获取历史任务: {}", processInstanceId);
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    // ========== 私有方法 - 对象转换 ==========

    private ProcessDefinitionDTO convertToProcessDefinitionDTO(ProcessDefinition definition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(definition.getId());
        dto.setKey(definition.getKey());
        dto.setName(definition.getName());
        dto.setVersion(definition.getVersion());
        dto.setDeploymentId(definition.getDeploymentId());
        dto.setResourceName(definition.getResourceName());
        dto.setSuspended(definition.isSuspended());
        return dto;
    }

    private ProcessInstanceDTO convertToProcessInstanceDTO(ProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(instance.getId());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId());
        // 获取流程定义Key需要通过流程定义ID查询
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId())
                .singleResult();
        if (processDefinition != null) {
            dto.setProcessDefinitionKey(processDefinition.getKey());
            dto.setProcessDefinitionName(processDefinition.getName());
        }
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setSuspended(instance.isSuspended());
        dto.setEnded(instance.isEnded());
        return dto;
    }

    private ProcessInstanceDTO convertToProcessInstanceDTO(HistoricProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setId(instance.getId());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId());
        dto.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setStartTime(instance.getStartTime());
        dto.setEndTime(instance.getEndTime());
        dto.setEnded(instance.getEndTime() != null);
        return dto;
    }

    private TaskDTO convertToTaskDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAssignee(task.getAssignee());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCreateTime(task.getCreateTime());
        dto.setDueDate(task.getDueDate());
        return dto;
    }

    private TaskDTO convertToTaskDTO(HistoricTaskInstance task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAssignee(task.getAssignee());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCreateTime(task.getStartTime());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}