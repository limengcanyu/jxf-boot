package com.spring.boot.activiti.service.impl;

import com.spring.boot.activiti.service.ITaskManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 任务管理服务实现类
 * 提供用户任务的查询、处理、委派等功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class TaskManagementServiceImpl implements ITaskManagementService {

    @Autowired
    private TaskService taskService;

    /**
     * 根据用户ID获取待办任务
     * 
     * @param assignee 用户ID
     * @return 任务列表
     */
    public List<Task> getTasksByAssignee(String assignee) {
        return taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
    }

    /**
     * 根据候选用户获取任务
     * 
     * @param candidateUser 候选用户
     * @return 任务列表
     */
    public List<Task> getTasksByCandidateUser(String candidateUser) {
        return taskService.createTaskQuery()
                .taskCandidateUser(candidateUser)
                .list();
    }

    /**
     * 根据候选组获取任务
     * 
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    public List<Task> getTasksByCandidateGroup(String candidateGroup) {
        return taskService.createTaskQuery()
                .taskCandidateGroup(candidateGroup)
                .list();
    }

    /**
     * 根据流程实例ID获取任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    public List<Task> getTasksByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 根据任务ID获取任务详情
     * 
     * @param taskId 任务ID
     * @return 任务对象
     */
    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
    }

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    /**
     * 完成任务（无变量）
     * 
     * @param taskId 任务ID
     */
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    /**
     * 取消认领任务
     * 
     * @param taskId 任务ID
     */
    public void unclaimTask(String taskId) {
        taskService.unclaim(taskId);
    }

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param userId 被委派用户ID
     */
    public void delegateTask(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }

    /**
     * 转派任务
     * 
     * @param taskId 任务ID
     * @param userId 新的处理人ID
     */
    public void assignTask(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    /**
     * 添加候选用户
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void addCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    /**
     * 添加候选组
     * 
     * @param taskId 任务ID
     * @param groupId 组ID
     */
    public void addCandidateGroup(String taskId, String groupId) {
        taskService.addCandidateGroup(taskId, groupId);
    }

    /**
     * 删除候选用户
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void deleteCandidateUser(String taskId, String userId) {
        taskService.deleteCandidateUser(taskId, userId);
    }

    /**
     * 删除候选组
     * 
     * @param taskId 任务ID
     * @param groupId 组ID
     */
    public void deleteCandidateGroup(String taskId, String groupId) {
        taskService.deleteCandidateGroup(taskId, groupId);
    }

    /**
     * 设置任务变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     */
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId, variables);
    }

    /**
     * 获取任务变量
     * 
     * @param taskId 任务ID
     * @return 变量Map
     */
    public Map<String, Object> getTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    /**
     * 设置任务本地变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     */
    public void setTaskVariablesLocal(String taskId, Map<String, Object> variables) {
        taskService.setVariablesLocal(taskId, variables);
    }

    /**
     * 获取任务本地变量
     * 
     * @param taskId 任务ID
     * @return 变量Map
     */
    public Map<String, Object> getTaskVariablesLocal(String taskId) {
        return taskService.getVariablesLocal(taskId);
    }

    /**
     * 添加任务评论
     * 
     * @param taskId 任务ID
     * @param processInstanceId 流程实例ID
     * @param message 评论内容
     */
    public void addTaskComment(String taskId, String processInstanceId, String message) {
        taskService.addComment(taskId, processInstanceId, message);
    }

    /**
     * 设置任务优先级
     * 
     * @param taskId 任务ID
     * @param priority 优先级
     */
    public void setTaskPriority(String taskId, int priority) {
        taskService.setPriority(taskId, priority);
    }

    /**
     * 创建任务查询构建器
     * 
     * @return TaskQuery对象
     */
    public TaskQuery createTaskQuery() {
        return taskService.createTaskQuery();
    }

    /**
     * 获取所有活动任务
     * 
     * @return 任务列表
     */
    public List<Task> getAllActiveTasks() {
        return taskService.createTaskQuery()
                .active()
                .list();
    }

    /**
     * 根据任务名称获取任务
     * 
     * @param taskName 任务名称
     * @return 任务列表
     */
    public List<Task> getTasksByName(String taskName) {
        return taskService.createTaskQuery()
                .taskName(taskName)
                .list();
    }
}