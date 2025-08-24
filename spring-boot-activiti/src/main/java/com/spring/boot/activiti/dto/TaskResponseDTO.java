package com.spring.boot.activiti.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务响应DTO
 * 
 * @author Auto Generated
 * @version 1.0
 */
public class TaskResponseDTO {

    private String taskId;
    private String taskName;
    private String processInstanceId;
    private String processDefinitionId;
    private String assignee;
    private String owner;
    private int priority;
    private LocalDateTime createTime;
    private LocalDateTime dueDate;
    private String description;
    private Map<String, Object> variables;

    // 构造方法
    public TaskResponseDTO() {
    }

    public TaskResponseDTO(String taskId, String taskName, String processInstanceId, String processDefinitionId,
                          String assignee, String owner, int priority, LocalDateTime createTime, 
                          LocalDateTime dueDate, String description, Map<String, Object> variables) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.assignee = assignee;
        this.owner = owner;
        this.priority = priority;
        this.createTime = createTime;
        this.dueDate = dueDate;
        this.description = description;
        this.variables = variables;
    }

    // Getter和Setter方法
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "TaskResponseDTO{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", assignee='" + assignee + '\'' +
                ", owner='" + owner + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", dueDate=" + dueDate +
                ", description='" + description + '\'' +
                ", variables=" + variables +
                '}';
    }
}