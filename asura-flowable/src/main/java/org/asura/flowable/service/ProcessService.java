package org.asura.flowable.service;

import org.asura.flowable.dto.request.ProcessStartRequest;
import org.asura.flowable.dto.request.TaskCompleteRequest;
import org.asura.flowable.dto.request.TaskQueryRequest;
import org.asura.flowable.dto.response.PageResponse;
import org.asura.flowable.dto.response.ProcessDefinitionDTO;
import org.asura.flowable.dto.response.ProcessInstanceDTO;
import org.asura.flowable.dto.response.ProcessTaskHistoryDTO;
import org.asura.flowable.dto.response.TaskDTO;

import java.util.List;
import java.util.Map;

/**
 * 流程服务接口
 */
public interface ProcessService {

    /**
     * 启动流程实例
     *
     * @param request 启动请求
     * @return 流程实例信息
     */
    ProcessInstanceDTO startProcess(ProcessStartRequest request);

    /**
     * 查询流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例信息
     */
    ProcessInstanceDTO getProcessInstance(String processInstanceId);

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void suspendProcess(String processInstanceId);

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void activateProcess(String processInstanceId);

    /**
     * 终止流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param reason            终止原因
     */
    void terminateProcess(String processInstanceId, String reason);

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * 设置流程变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables         流程变量Map
     */
    void setProcessVariables(String processInstanceId, Map<String, Object> variables);

    /**
     * 查询待办任务列表
     *
     * @param request 查询请求
     * @param page    页码
     * @param size    每页大小
     * @return 分页任务列表
     */
    PageResponse<TaskDTO> queryTasks(TaskQueryRequest request, Integer page, Integer size);

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    TaskDTO getTask(String taskId);

    /**
     * 获取任务变量
     *
     * @param taskId 任务ID
     * @return 任务变量Map
     */
    Map<String, Object> getTaskVariables(String taskId);

    /**
     * 签收任务
     *
     * @param taskId   任务ID
     * @param assignee 办理人
     */
    void claimTask(String taskId, String assignee);

    /**
     * 取消签收任务
     *
     * @param taskId 任务ID
     */
    void unclaimTask(String taskId);

    /**
     * 完成任务
     *
     * @param request 完成请求
     */
    void completeTask(TaskCompleteRequest request);

    /**
     * 委派任务
     *
     * @param taskId   任务ID
     * @param assignee 被委派人
     * @param comment  备注
     */
    void delegateTask(String taskId, String assignee, String comment);

    /**
     * 转办任务
     *
     * @param taskId   任务ID
     * @param assignee 新办理人
     */
    void assignTask(String taskId, String assignee);

    /**
     * 查询流程定义列表
     *
     * @param processDefinitionKey 流程定义Key(可选)
     * @param page                 页码
     * @param size                 每页大小
     * @return 分页流程定义列表
     */
    PageResponse<ProcessDefinitionDTO> queryProcessDefinitions(String processDefinitionKey, Integer page, Integer size);

    /**
     * 获取流程定义详情
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程定义详情
     */
    ProcessDefinitionDTO getProcessDefinition(String processDefinitionId);

    /**
     * 部署流程定义
     *
     * @param bpmnXml BPMN XML内容
     * @param processName 流程名称
     * @return 流程定义ID
     */
    String deployProcess(String bpmnXml, String processName);

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

    /**
     * 获取流程历史任务列表
     *
     * @param processInstanceId 流程实例ID
     * @return 历史任务列表
     */
    List<ProcessTaskHistoryDTO> getProcessHistoryTasks(String processInstanceId);

    /**
     * 获取流程图片
     *
     * @param processDefinitionId 流程定义ID
     * @return 图片字节数组
     */
    byte[] getProcessDiagram(String processDefinitionId);

    /**
     * 获取流程实例当前状态图片
     *
     * @param processInstanceId 流程实例ID
     * @return 图片字节数组
     */
    byte[] getProcessInstanceDiagram(String processInstanceId);
}