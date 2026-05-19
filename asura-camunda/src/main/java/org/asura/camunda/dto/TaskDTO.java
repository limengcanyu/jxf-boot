package org.asura.camunda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Map;

/**
 * 任务响应DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class TaskDTO {
    
    /**
     * 任务ID
     */
    private String id;
    
    /**
     * 任务名称
     */
    private String name;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 任务分配人
     */
    private String assignee;
    
    /**
     * 任务候选用户
     */
    private String candidateUsers;
    
    /**
     * 任务候选组
     */
    private String candidateGroups;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    
    /**
     * 流程定义Key
     */
    private String processDefinitionKey;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dueDate;
    
    /**
     * 任务变量
     */
    private Map<String, Object> variables;

    // Constructors
    public TaskDTO() {}

    public TaskDTO(String id, String name, String assignee, String processInstanceId) {
        this.id = id;
        this.name = name;
        this.assignee = assignee;
        this.processInstanceId = processInstanceId;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(String candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(String candidateGroups) {
        this.candidateGroups = candidateGroups;
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

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", assignee='" + assignee + '\'' +
                ", candidateUsers='" + candidateUsers + '\'' +
                ", candidateGroups='" + candidateGroups + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", createTime=" + createTime +
                ", dueDate=" + dueDate +
                ", variables=" + variables +
                '}';
    }
}