package com.spring.boot.camunda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Map;

/**
 * 流程实例响应DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class ProcessInstanceDTO {
    
    /**
     * 流程实例ID
     */
    private String id;
    
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    
    /**
     * 流程定义Key
     */
    private String processDefinitionKey;
    
    /**
     * 流程定义名称
     */
    private String processDefinitionName;
    
    /**
     * 业务Key
     */
    private String businessKey;
    
    /**
     * 是否暂停
     */
    private Boolean suspended;
    
    /**
     * 是否结束
     */
    private Boolean ended;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    // Constructors
    public ProcessInstanceDTO() {}

    public ProcessInstanceDTO(String id, String processDefinitionId, String businessKey) {
        this.id = id;
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ProcessInstanceDTO{" +
                "id='" + id + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", suspended=" + suspended +
                ", ended=" + ended +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", variables=" + variables +
                '}';
    }
}