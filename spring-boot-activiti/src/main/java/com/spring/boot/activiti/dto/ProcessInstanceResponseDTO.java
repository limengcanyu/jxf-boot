package com.spring.boot.activiti.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程实例响应DTO
 * 
 * @author Auto Generated
 * @version 1.0
 */
public class ProcessInstanceResponseDTO {

    private String processInstanceId;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private String businessKey;
    private String startUserId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String deleteReason;
    private boolean isEnded;
    private boolean isSuspended;
    private Map<String, Object> variables;

    // 构造方法
    public ProcessInstanceResponseDTO() {
    }

    public ProcessInstanceResponseDTO(String processInstanceId, String processDefinitionId, 
                                    String processDefinitionKey, String processDefinitionName, 
                                    String businessKey, String startUserId, LocalDateTime startTime, 
                                    LocalDateTime endTime, String deleteReason, boolean isEnded, 
                                    boolean isSuspended, Map<String, Object> variables) {
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionName = processDefinitionName;
        this.businessKey = businessKey;
        this.startUserId = startUserId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleteReason = deleteReason;
        this.isEnded = isEnded;
        this.isSuspended = isSuspended;
        this.variables = variables;
    }

    // Getter和Setter方法
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

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "ProcessInstanceResponseDTO{" +
                "processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", startUserId='" + startUserId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", deleteReason='" + deleteReason + '\'' +
                ", isEnded=" + isEnded +
                ", isSuspended=" + isSuspended +
                ", variables=" + variables +
                '}';
    }
}