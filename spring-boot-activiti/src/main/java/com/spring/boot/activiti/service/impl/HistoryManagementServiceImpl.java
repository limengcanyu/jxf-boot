package com.spring.boot.activiti.service.impl;

import com.spring.boot.activiti.service.IHistoryManagementService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 历史管理服务实现类
 * 提供流程历史数据查询功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class HistoryManagementServiceImpl implements IHistoryManagementService {

    @Autowired
    private HistoryService historyService;

    /**
     * 获取历史流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 历史流程实例列表
     */
    public List<HistoricProcessInstance> getHistoricProcessInstances(String processDefinitionKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
    }

    /**
     * 根据流程实例ID获取历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例
     */
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    /**
     * 获取已完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    public List<HistoricProcessInstance> getFinishedProcessInstances() {
        return historyService.createHistoricProcessInstanceQuery()
                .finished()
                .list();
    }

    /**
     * 获取未完成的历史流程实例
     * 
     * @return 历史流程实例列表
     */
    public List<HistoricProcessInstance> getUnfinishedProcessInstances() {
        return historyService.createHistoricProcessInstanceQuery()
                .unfinished()
                .list();
    }

    /**
     * 根据启动用户获取历史流程实例
     * 
     * @param startedBy 启动用户
     * @return 历史流程实例列表
     */
    public List<HistoricProcessInstance> getHistoricProcessInstancesByStarter(String startedBy) {
        return historyService.createHistoricProcessInstanceQuery()
                .startedBy(startedBy)
                .list();
    }

    /**
     * 根据业务Key获取历史流程实例
     * 
     * @param businessKey 业务Key
     * @return 历史流程实例
     */
    public HistoricProcessInstance getHistoricProcessInstanceByBusinessKey(String businessKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    /**
     * 获取历史任务实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史任务实例列表
     */
    public List<HistoricTaskInstance> getHistoricTaskInstances(String processInstanceId) {
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 根据任务处理人获取历史任务
     * 
     * @param assignee 处理人
     * @return 历史任务实例列表
     */
    public List<HistoricTaskInstance> getHistoricTaskInstancesByAssignee(String assignee) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .list();
    }

    /**
     * 获取已完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    public List<HistoricTaskInstance> getFinishedHistoricTaskInstances() {
        return historyService.createHistoricTaskInstanceQuery()
                .finished()
                .list();
    }

    /**
     * 获取未完成的历史任务
     * 
     * @return 历史任务实例列表
     */
    public List<HistoricTaskInstance> getUnfinishedHistoricTaskInstances() {
        return historyService.createHistoricTaskInstanceQuery()
                .unfinished()
                .list();
    }

    /**
     * 根据任务名称获取历史任务
     * 
     * @param taskName 任务名称
     * @return 历史任务实例列表
     */
    public List<HistoricTaskInstance> getHistoricTaskInstancesByName(String taskName) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskName(taskName)
                .list();
    }

    /**
     * 获取历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    public List<HistoricActivityInstance> getHistoricActivityInstances(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 根据活动ID获取历史活动实例
     * 
     * @param activityId 活动ID
     * @return 历史活动实例列表
     */
    public List<HistoricActivityInstance> getHistoricActivityInstancesByActivityId(String activityId) {
        return historyService.createHistoricActivityInstanceQuery()
                .activityId(activityId)
                .list();
    }

    /**
     * 获取已完成的历史活动实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    public List<HistoricActivityInstance> getFinishedHistoricActivityInstances(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
    }

    /**
     * 获取历史变量实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史变量实例列表
     */
    public List<HistoricVariableInstance> getHistoricVariableInstances(String processInstanceId) {
        return historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 根据变量名获取历史变量实例
     * 
     * @param variableName 变量名
     * @return 历史变量实例列表
     */
    public List<HistoricVariableInstance> getHistoricVariableInstancesByName(String variableName) {
        return historyService.createHistoricVariableInstanceQuery()
                .variableName(variableName)
                .list();
    }

    /**
     * 删除历史流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    public void deleteHistoricProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    /**
     * 删除历史任务实例
     * 
     * @param taskId 任务ID
     */
    public void deleteHistoricTaskInstance(String taskId) {
        historyService.deleteHistoricTaskInstance(taskId);
    }

    /**
     * 根据流程定义获取统计信息
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例数量
     */
    public long getProcessInstanceCount(String processDefinitionKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .count();
    }

    /**
     * 获取已完成流程实例数量
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 已完成流程实例数量
     */
    public long getFinishedProcessInstanceCount(String processDefinitionKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .finished()
                .count();
    }
}