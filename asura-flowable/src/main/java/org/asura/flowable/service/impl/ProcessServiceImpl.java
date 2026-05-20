package org.asura.flowable.service.impl;

import jakarta.annotation.Resource;
import org.asura.flowable.dto.request.ProcessStartRequest;
import org.asura.flowable.dto.request.TaskCompleteRequest;
import org.asura.flowable.dto.request.TaskQueryRequest;
import org.asura.flowable.dto.response.*;
import org.asura.flowable.enums.ProcessStatusEnum;
import org.asura.flowable.enums.TaskActionEnum;
import org.asura.flowable.exception.ProcessException;
import org.asura.flowable.service.ProcessService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private ProcessEngine processEngine;

    @Override
    public ProcessInstanceDTO startProcess(ProcessStartRequest request) {
        Map<String, Object> variables = request.getVariables();
        if (variables == null) {
            variables = new HashMap<>();
        }

        if (StringUtils.hasText(request.getInitiator())) {
            variables.put("initiator", request.getInitiator());
        }

        org.flowable.engine.runtime.ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                request.getProcessDefinitionKey(),
                request.getBusinessKey(),
                variables
        );

        return convertToProcessInstanceDTO(processInstance);
    }

    @Override
    public ProcessInstanceDTO getProcessInstance(String processInstanceId) {
        org.flowable.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            throw new ProcessException("PROCESS_NOT_FOUND", "流程实例不存在: " + processInstanceId);
        }

        return convertToProcessInstanceDTO(processInstance);
    }

    @Override
    public void suspendProcess(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void activateProcess(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public void terminateProcess(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TaskDTO> queryTasks(TaskQueryRequest request, Integer page, Integer size) {
        if (request == null) {
            request = new TaskQueryRequest();
        }

        TaskQuery query = taskService.createTaskQuery();

        if (StringUtils.hasText(request.getTaskName())) {
            query.taskNameLike("%" + request.getTaskName() + "%");
        }
        if (StringUtils.hasText(request.getTaskKey())) {
            query.taskDefinitionKey(request.getTaskKey());
        }
        if (StringUtils.hasText(request.getProcessInstanceId())) {
            query.processInstanceId(request.getProcessInstanceId());
        }
        if (StringUtils.hasText(request.getProcessDefinitionKey())) {
            query.processDefinitionKey(request.getProcessDefinitionKey());
        }
        if (StringUtils.hasText(request.getBusinessKey())) {
            query.processInstanceBusinessKey(request.getBusinessKey());
        }
        if (StringUtils.hasText(request.getAssignee())) {
            query.taskAssignee(request.getAssignee());
        }
        if (StringUtils.hasText(request.getCandidateUser())) {
            query.taskCandidateUser(request.getCandidateUser());
        }
        if (StringUtils.hasText(request.getCandidateGroup())) {
            query.taskCandidateGroup(request.getCandidateGroup());
        }

        long total = query.count();
        List<Task> tasks = query.orderByTaskCreateTime().desc()
                .listPage((page - 1) * size, size);

        List<TaskDTO> taskDTOList = tasks.stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());

        PageResponse<TaskDTO> response = new PageResponse<>();
        response.setContent(taskDTOList);
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(total);
        response.setTotalPages((int) Math.ceil((double) total / size));
        response.setFirst(page == 1);
        response.setLast(page >= (int) Math.ceil((double) total / size));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTask(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new ProcessException("TASK_NOT_FOUND", "任务不存在: " + taskId);
        }

        return convertToTaskDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    public void claimTask(String taskId, String assignee) {
        taskService.claim(taskId, assignee);
    }

    @Override
    public void unclaimTask(String taskId) {
        taskService.unclaim(taskId);
    }

    @Override
    public void completeTask(TaskCompleteRequest request) {
        String action = request.getAction();
        TaskActionEnum actionEnum = TaskActionEnum.fromCode(action);
        if (actionEnum == null) {
            throw new ProcessException("INVALID_ACTION", "无效的操作类型: " + action);
        }

        Map<String, Object> variables = request.getVariables();
        if (variables == null) {
            variables = new HashMap<>();
        }

        switch (actionEnum) {
            case APPROVE:
                variables.put("approved", true);
                break;
            case REJECT:
                variables.put("approved", false);
                break;
            default:
                break;
        }

        if (StringUtils.hasText(request.getComment())) {
            taskService.addComment(request.getTaskId(), null, request.getComment());
        }

        taskService.complete(request.getTaskId(), variables);
    }

    @Override
    public void delegateTask(String taskId, String assignee, String comment) {
        taskService.delegateTask(taskId, assignee);
        if (StringUtils.hasText(comment)) {
            taskService.addComment(taskId, null, "委派: " + comment);
        }
    }

    @Override
    public void assignTask(String taskId, String assignee) {
        taskService.setAssignee(taskId, assignee);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProcessDefinitionDTO> queryProcessDefinitions(String processDefinitionKey,
                                                                       Integer page, Integer size) {
        org.flowable.engine.repository.ProcessDefinitionQuery query =
                repositoryService.createProcessDefinitionQuery();

        if (StringUtils.hasText(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }

        long total = query.count();
        List<org.flowable.engine.repository.ProcessDefinition> definitions = query.orderByProcessDefinitionVersion().desc()
                .listPage((page - 1) * size, size);

        List<ProcessDefinitionDTO> dtoList = definitions.stream()
                .map(this::convertToProcessDefinitionDTO)
                .collect(Collectors.toList());

        PageResponse<ProcessDefinitionDTO> response = new PageResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(total);
        response.setTotalPages((int) Math.ceil((double) total / size));
        response.setFirst(page == 1);
        response.setLast(page >= (int) Math.ceil((double) total / size));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessDefinitionDTO getProcessDefinition(String processDefinitionId) {
        org.flowable.engine.repository.ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        if (definition == null) {
            throw new ProcessException("DEFINITION_NOT_FOUND",
                    "流程定义不存在: " + processDefinitionId);
        }

        return convertToProcessDefinitionDTO(definition);
    }

    @Override
    public String deployProcess(String bpmnXml, String processName) {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeployment()
                .addString(processName + ".bpmn20.xml", bpmnXml)
                .name(processName)
                .deploy();

        List<org.flowable.engine.repository.ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .list();

        return definitions.isEmpty() ? null : definitions.getFirst().getId();
    }

    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessTaskHistoryDTO> getProcessHistoryTasks(String processInstanceId) {
        List<HistoricTaskInstance> historyTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        Map<String, String> commentMap = new HashMap<>();
        List<?> comments = historyService.createHistoricDetailQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (Object obj : comments) {
            if (obj instanceof org.flowable.engine.history.HistoricDetail detail) {
                if (detail instanceof org.flowable.engine.history.HistoricVariableUpdate) {
                    continue;
                }
            }
        }

        return historyTasks.stream()
                .map(task -> convertToProcessTaskHistoryDTO(task, commentMap))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getProcessDiagram(String processDefinitionId) {
        org.flowable.engine.repository.ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        if (definition == null) {
            throw new ProcessException("DEFINITION_NOT_FOUND",
                    "流程定义不存在: " + processDefinitionId);
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
        ProcessEngineConfiguration config = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator generator = config.getProcessDiagramGenerator();

        InputStream inputStream = generator.generateDiagram(
                bpmnModel,
                "png",
                new ArrayList<>(),
                new ArrayList<>(),
                config.getActivityFontName(),
                config.getLabelFontName(),
                config.getAnnotationFontName(),
                config.getClassLoader(),
                1.0,
                false
        );

        return toByteArray(inputStream);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getProcessInstanceDiagram(String processInstanceId) {
        org.flowable.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            throw new ProcessException("PROCESS_NOT_FOUND", "流程实例不存在: " + processInstanceId);
        }

        org.flowable.engine.repository.ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        ProcessEngineConfiguration config = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator generator = config.getProcessDiagramGenerator();

        InputStream inputStream = generator.generateDiagram(
                bpmnModel,
                "png",
                activeActivityIds,
                new ArrayList<>(),
                config.getActivityFontName(),
                config.getLabelFontName(),
                config.getAnnotationFontName(),
                config.getClassLoader(),
                1.0,
                false
        );

        return toByteArray(inputStream);
    }

    private ProcessInstanceDTO convertToProcessInstanceDTO(org.flowable.engine.runtime.ProcessInstance processInstance) {
        org.flowable.engine.repository.ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        String status = processInstance.isEnded()
                ? ProcessStatusEnum.COMPLETED.getCode()
                : (processInstance.isSuspended()
                ? ProcessStatusEnum.SUSPENDED.getCode()
                : ProcessStatusEnum.RUNNING.getCode());

        ProcessStatusEnum statusEnum = ProcessStatusEnum.fromCode(status);

        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        dto.setProcessInstanceId(processInstance.getId());
        dto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        dto.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        dto.setProcessDefinitionName(definition != null ? definition.getName() : null);
        dto.setBusinessKey(processInstance.getBusinessKey());
        dto.setStatus(status);
        dto.setStatusDesc(statusEnum != null ? statusEnum.getDesc() : null);
        dto.setInitiator((String) runtimeService.getVariable(processInstance.getId(), "initiator"));
        dto.setVariables(runtimeService.getVariables(processInstance.getId()));
        dto.setStartTime(toLocalDateTime(processInstance.getStartTime()));
        dto.setEndTime(null);
        return dto;
    }

    private TaskDTO convertToTaskDTO(Task task) {
        org.flowable.engine.repository.ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        List<String> candidateUsers = new ArrayList<>();
        List<String> candidateGroups = new ArrayList<>();
        
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getId());
        dto.setTaskName(task.getName());
        dto.setTaskKey(task.getTaskDefinitionKey());
        dto.setDescription(task.getDescription());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setProcessDefinitionKey(definition != null ? definition.getKey() : null);
        dto.setProcessDefinitionName(definition != null ? definition.getName() : null);
        dto.setBusinessKey(getBusinessKey(task.getProcessInstanceId()));
        dto.setAssignee(task.getAssignee());
        dto.setCandidateUsers(candidateUsers);
        dto.setCandidateGroups(candidateGroups);
        dto.setCreateTime(toLocalDateTime(task.getCreateTime()));
        dto.setDueDate(toLocalDateTime(task.getDueDate()));
        dto.setPriority(task.getPriority());
        dto.setVariables(taskService.getVariables(task.getId()));
        return dto;
    }

    private ProcessDefinitionDTO convertToProcessDefinitionDTO(org.flowable.engine.repository.ProcessDefinition definition) {
        ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
        dto.setProcessDefinitionId(definition.getId());
        dto.setProcessDefinitionKey(definition.getKey());
        dto.setProcessDefinitionName(definition.getName());
        dto.setVersion(definition.getVersion());
        dto.setDescription(definition.getDescription());
        dto.setDeploymentId(definition.getDeploymentId());
        dto.setResourceName(definition.getResourceName());
        dto.setIsActive(!definition.isSuspended());
        dto.setIsSuspended(definition.isSuspended());
        dto.setDeploymentTime(toLocalDateTime(getDeploymentTime(definition.getDeploymentId())));
        return dto;
    }

    private ProcessTaskHistoryDTO convertToProcessTaskHistoryDTO(HistoricTaskInstance task,
                                                                Map<String, String> commentMap) {
        ProcessTaskHistoryDTO dto = new ProcessTaskHistoryDTO();
        dto.setTaskId(task.getId());
        dto.setTaskName(task.getName());
        dto.setTaskKey(task.getTaskDefinitionKey());
        dto.setAssignee(task.getAssignee());
        dto.setStartTime(toLocalDateTime(task.getCreateTime()));
        dto.setEndTime(toLocalDateTime(task.getEndTime()));
        dto.setDurationInMillis(task.getDurationInMillis());
        dto.setComment(commentMap.get(task.getId()));
        dto.setActionResult(task.getEndTime() != null ? "已完成" : "未完成");
        return dto;
    }

    private String getBusinessKey(String processInstanceId) {
        try {
            org.flowable.engine.runtime.ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            return instance != null ? instance.getBusinessKey() : null;
        } catch (Exception e) {
            org.flowable.engine.history.HistoricProcessInstance historicInstance = 
                    historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            return historicInstance != null ? historicInstance.getBusinessKey() : null;
        }
    }

    private LocalDateTime toLocalDateTime(java.util.Date date) {
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    private java.util.Date getDeploymentTime(String deploymentId) {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
        return deployment != null ? deployment.getDeploymentTime() : null;
    }

    private byte[] toByteArray(InputStream inputStream) {
        if (inputStream == null) {
            return new byte[0];
        }
        try (inputStream; ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputStream.toByteArray();
            } catch (Exception e) {
                throw new ProcessException("DIAGRAM_ERROR", "图片生成失败", e);
            }
        } catch (Exception _) {
        }
        return new byte[0];
    }
}