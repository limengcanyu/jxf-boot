package org.akuma.activiti.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;

import java.util.List;

/**
 * 历史管理服务接口
 * 提供流程历史数据查询功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
public interface IHistoryManagementService {

    /**
     * 获取历史流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 历史流程实例列表
     */
    List<HistoricProcessInstance> getHistoricProcessInstances(String processDefinitionKey);

    /**
     * 根据流程实例ID获取历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例
     */
    HistoricProcessInstance getHistoricProcessInstance(String processInstanceId);

    /**
     * 获取已完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    List<HistoricProcessInstance> getFinishedProcessInstances();

    /**
     * 获取未完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    List<HistoricProcessInstance> getUnfinishedProcessInstances();

    /**
     * 根据启动用户获取历史流程实例
     * 
     * @param startedBy 启动用户
     * @return 历史流程实例列表
     */
    List<HistoricProcessInstance> getHistoricProcessInstancesByStarter(String startedBy);

    /**
     * 根据业务Key获取历史流程实例
     * 
     * @param businessKey 业务Key
     * @return 历史流程实例
     */
    HistoricProcessInstance getHistoricProcessInstanceByBusinessKey(String businessKey);

    /**
     * 获取历史任务实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史任务实例列表
     */
    List<HistoricTaskInstance> getHistoricTaskInstances(String processInstanceId);

    /**
     * 根据任务处理人获取历史任务
     * 
     * @param assignee 处理人
     * @return 历史任务实例列表
     */
    List<HistoricTaskInstance> getHistoricTaskInstancesByAssignee(String assignee);

    /**
     * 获取已完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    List<HistoricTaskInstance> getFinishedHistoricTaskInstances();

    /**
     * 获取未完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    List<HistoricTaskInstance> getUnfinishedHistoricTaskInstances();

    /**
     * 根据任务名称获取历史任务
     * 
     * @param taskName 任务名称
     * @return 历史任务实例列表
     */
    List<HistoricTaskInstance> getHistoricTaskInstancesByName(String taskName);

    /**
     * 获取历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    List<HistoricActivityInstance> getHistoricActivityInstances(String processInstanceId);

    /**
     * 根据活动ID获取历史活动实例
     * 
     * @param activityId 活动ID
     * @return 历史活动实例列表
     */
    List<HistoricActivityInstance> getHistoricActivityInstancesByActivityId(String activityId);

    /**
     * 获取已完成的历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    List<HistoricActivityInstance> getFinishedHistoricActivityInstances(String processInstanceId);

    /**
     * 获取历史变量实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史变量实例列表
     */
    List<HistoricVariableInstance> getHistoricVariableInstances(String processInstanceId);

    /**
     * 根据变量名获取历史变量实例
     * 
     * @param variableName 变量名
     * @return 历史变量实例列表
     */
    List<HistoricVariableInstance> getHistoricVariableInstancesByName(String variableName);

    /**
     * 删除历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    void deleteHistoricProcessInstance(String processInstanceId);

    /**
     * 删除历史任务实例
     * 
     * @param taskId 任务ID
     */
    void deleteHistoricTaskInstance(String taskId);

    /**
     * 根据流程定义获取统计信息
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例数量
     */
    long getProcessInstanceCount(String processDefinitionKey);

    /**
     * 获取已完成流程实例数量
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 已完成流程实例数量
     */
    long getFinishedProcessInstanceCount(String processDefinitionKey);
}