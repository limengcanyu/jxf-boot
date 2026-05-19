package org.asura.activiti.service.impl;

import org.asura.activiti.entity.ProcessDefinitionDTO;
import org.asura.activiti.entity.ProcessInstanceDTO;
import org.asura.activiti.entity.TaskDTO;
import org.asura.activiti.service.ProcessService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl implements ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;

    public ProcessServiceImpl(RepositoryService repositoryService, RuntimeService runtimeService,
                              TaskService taskService, HistoryService historyService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
    }

    @Override
    public List<ProcessDefinitionDTO> getProcessDefinitions() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey()
                .asc()
                .list();
        
        return definitions.stream()
                .map(this::convertToProcessDefinitionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getProcessDiagram(String processDefinitionId) {
        return new byte[0];
    }

    @Override
    @Transactional
    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        logger.info("Starting process: {} with businessKey: {}", processDefinitionKey, businessKey);
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                processDefinitionKey,
                businessKey,
                variables
        );
        
        logger.info("Process started successfully: {}", processInstance.getId());
        return processInstance.getId();
    }

    @Override
    public ProcessInstanceDTO getProcessInstance(String processInstanceId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        if (instance != null) {
            return convertToProcessInstanceDTO(instance);
        }
        
        HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        if (historicInstance != null) {
            return convertToHistoricProcessInstanceDTO(historicInstance);
        }
        
        return null;
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstances(String processDefinitionKey) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        
        return instances.stream()
                .map(this::convertToProcessInstanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        logger.info("Deleting process instance: {} with reason: {}", processInstanceId, deleteReason);
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    public List<TaskDTO> getTasksByAssignee(String assignee) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .desc()
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
        
        return tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTask(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        
        return task != null ? convertToTaskDTO(task) : null;
    }

    @Override
    @Transactional
    public void completeTask(String taskId, Map<String, Object> variables) {
        logger.info("Completing task: {}", taskId);
        
        if (variables != null && !variables.isEmpty()) {
            taskService.setVariables(taskId, variables);
        }
        
        taskService.complete(taskId);
        logger.info("Task completed successfully: {}", taskId);
    }

    @Override
    @Transactional
    public void claimTask(String taskId, String userId) {
        logger.info("Claiming task: {} for user: {}", taskId, userId);
        taskService.claim(taskId, userId);
    }

    @Override
    @Transactional
    public void delegateTask(String taskId, String userId) {
        logger.info("Delegating task: {} to user: {}", taskId, userId);
        taskService.delegateTask(taskId, userId);
    }

    private ProcessDefinitionDTO convertToProcessDefinitionDTO(ProcessDefinition definition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setId(definition.getId());
        dto.setName(definition.getName());
        dto.setKey(definition.getKey());
        dto.setVersion(definition.getVersion());
        dto.setResourceName(definition.getResourceName());
        dto.setDiagramResourceName(definition.getDiagramResourceName());
        dto.setSuspended(definition.isSuspended());
        return dto;
    }

    private ProcessInstanceDTO convertToProcessInstanceDTO(ProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setProcessInstanceId(instance.getId());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setStartTime(instance.getStartTime());
        dto.setStatus(instance.isEnded() ? "ENDED" : "RUNNING");
        dto.setVariables(runtimeService.getVariables(instance.getId()));
        return dto;
    }

    private ProcessInstanceDTO convertToHistoricProcessInstanceDTO(HistoricProcessInstance instance) {
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setProcessInstanceId(instance.getId());
        dto.setProcessDefinitionId(instance.getProcessDefinitionId());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setStartUserId(instance.getStartUserId());
        dto.setStartTime(instance.getStartTime());
        dto.setEndTime(instance.getEndTime());
        dto.setStatus("COMPLETED");
        dto.setVariables(new HashMap<>());
        return dto;
    }

    private TaskDTO convertToTaskDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getId());
        dto.setName(task.getName());
        dto.setAssignee(task.getAssignee());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCreateTime(task.getCreateTime());
        dto.setDueDate(task.getDueDate());
        dto.setDescription(task.getDescription());
        dto.setVariables(taskService.getVariables(task.getId()));
        return dto;
    }
}