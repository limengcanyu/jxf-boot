package org.asura.camunda.service;

import org.asura.camunda.dto.LeaveRequestDTO;
import org.asura.camunda.dto.ProcessDefinitionDTO;
import org.asura.camunda.dto.ProcessInstanceDTO;
import org.asura.camunda.dto.TaskDTO;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务接口
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public interface IWorkflowService {

    // ========== 流程定义管理 ==========

    /**
     * 获取所有流程定义
     * 
     * @return 流程定义列表
     */
    List<ProcessDefinitionDTO> getAllProcessDefinitions();

    /**
     * 根据Key获取流程定义
     * 
     * @param key 流程定义Key
     * @return 流程定义信息
     */
    ProcessDefinitionDTO getProcessDefinitionByKey(String key);

    /**
     * 部署流程定义
     * 
     * @param resourceName 资源名称
     * @param bpmnContent BPMN内容
     * @return 部署ID
     */
    String deployProcessDefinition(String resourceName, String bpmnContent);

    /**
     * 挂起流程定义
     * 
     * @param processDefinitionId 流程定义ID
     */
    void suspendProcessDefinition(String processDefinitionId);

    /**
     * 激活流程定义
     * 
     * @param processDefinitionId 流程定义ID
     */
    void activateProcessDefinition(String processDefinitionId);

    // ========== 流程实例管理 ==========

    /**
     * 启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例信息
     */
    ProcessInstanceDTO startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * 启动请假流程
     * 
     * @param leaveRequest 请假申请
     * @return 流程实例信息
     */
    ProcessInstanceDTO startLeaveProcess(LeaveRequestDTO leaveRequest);

    /**
     * 获取所有流程实例
     * 
     * @return 流程实例列表
     */
    List<ProcessInstanceDTO> getAllProcessInstances();

    /**
     * 根据ID获取流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程实例信息
     */
    ProcessInstanceDTO getProcessInstanceById(String processInstanceId);

    /**
     * 删除流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param reason 删除原因
     */
    void deleteProcessInstance(String processInstanceId, String reason);

    // ========== 任务管理 ==========

    /**
     * 获取所有任务
     * 
     * @return 任务列表
     */
    List<TaskDTO> getAllTasks();

    /**
     * 根据分配人获取任务
     * 
     * @param assignee 分配人
     * @return 任务列表
     */
    List<TaskDTO> getTasksByAssignee(String assignee);

    /**
     * 根据候选用户获取任务
     * 
     * @param candidateUser 候选用户
     * @return 任务列表
     */
    List<TaskDTO> getTasksByCandidateUser(String candidateUser);

    /**
     * 根据流程实例ID获取任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId);

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 任务变量
     * @param comment 评论
     */
    void completeTask(String taskId, Map<String, Object> variables, String comment);

    /**
     * 分配任务
     * 
     * @param taskId 任务ID
     * @param assignee 分配人
     */
    void assignTask(String taskId, String assignee);

    /**
     * 取消分配任务
     * 
     * @param taskId 任务ID
     */
    void unclaimTask(String taskId);

    // ========== 历史记录查询 ==========

    /**
     * 获取历史流程实例
     * 
     * @return 历史流程实例列表
     */
    List<ProcessInstanceDTO> getHistoricProcessInstances();

    /**
     * 根据流程定义Key获取历史流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 历史流程实例列表
     */
    List<ProcessInstanceDTO> getHistoricProcessInstancesByKey(String processDefinitionKey);

    /**
     * 获取历史任务
     * 
     * @return 历史任务列表
     */
    List<TaskDTO> getHistoricTasks();

    /**
     * 根据流程实例ID获取历史任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史任务列表
     */
    List<TaskDTO> getHistoricTasksByProcessInstanceId(String processInstanceId);
}