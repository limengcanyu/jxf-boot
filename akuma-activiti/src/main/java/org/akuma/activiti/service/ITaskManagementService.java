package org.akuma.activiti.service;

import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;
import java.util.Map;

/**
 * 任务管理服务接口
 * 提供用户任务的查询、处理、委派等功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
public interface ITaskManagementService {

    /**
     * 根据用户ID获取待办任务
     * 
     * @param assignee 用户ID
     * @return 任务列表
     */
    List<Task> getTasksByAssignee(String assignee);

    /**
     * 根据候选用户获取任务
     * 
     * @param candidateUser 候选用户
     * @return 任务列表
     */
    List<Task> getTasksByCandidateUser(String candidateUser);

    /**
     * 根据候选组获取任务
     * 
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    List<Task> getTasksByCandidateGroup(String candidateGroup);

    /**
     * 根据流程实例ID获取任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<Task> getTasksByProcessInstanceId(String processInstanceId);

    /**
     * 根据任务ID获取任务详情
     * 
     * @param taskId 任务ID
     * @return 任务对象
     */
    Task getTaskById(String taskId);

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * 完成任务（无变量）
     * 
     * @param taskId 任务ID
     */
    void completeTask(String taskId);

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    void claimTask(String taskId, String userId);

    /**
     * 取消认领任务
     * 
     * @param taskId 任务ID
     */
    void unclaimTask(String taskId);

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param userId 被委派用户ID
     */
    void delegateTask(String taskId, String userId);

    /**
     * 转派任务
     * 
     * @param taskId 任务ID
     * @param userId 新的处理人ID
     */
    void assignTask(String taskId, String userId);

    /**
     * 添加候选用户
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    void addCandidateUser(String taskId, String userId);

    /**
     * 添加候选组
     * 
     * @param taskId 任务ID
     * @param groupId 组ID
     */
    void addCandidateGroup(String taskId, String groupId);

    /**
     * 删除候选用户
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    void deleteCandidateUser(String taskId, String userId);

    /**
     * 删除候选组
     * 
     * @param taskId 任务ID
     * @param groupId 组ID
     */
    void deleteCandidateGroup(String taskId, String groupId);

    /**
     * 设置任务变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     */
    void setTaskVariables(String taskId, Map<String, Object> variables);

    /**
     * 获取任务变量
     * 
     * @param taskId 任务ID
     * @return 变量Map
     */
    Map<String, Object> getTaskVariables(String taskId);

    /**
     * 设置任务本地变量
     * 
     * @param taskId 任务ID
     * @param variables 变量Map
     */
    void setTaskVariablesLocal(String taskId, Map<String, Object> variables);

    /**
     * 获取任务本地变量
     * 
     * @param taskId 任务ID
     * @return 变量Map
     */
    Map<String, Object> getTaskVariablesLocal(String taskId);

    /**
     * 添加任务评论
     * 
     * @param taskId 任务ID
     * @param processInstanceId 流程实例ID
     * @param message 评论内容
     */
    void addTaskComment(String taskId, String processInstanceId, String message);

    /**
     * 设置任务优先级
     * 
     * @param taskId 任务ID
     * @param priority 优先级
     */
    void setTaskPriority(String taskId, int priority);

    /**
     * 创建任务查询构建器
     * 
     * @return TaskQuery对象
     */
    TaskQuery createTaskQuery();

    /**
     * 获取所有活动任务
     * 
     * @return 任务列表
     */
    List<Task> getAllActiveTasks();

    /**
     * 根据任务名称获取任务
     * 
     * @param taskName 任务名称
     * @return 任务列表
     */
    List<Task> getTasksByName(String taskName);
}