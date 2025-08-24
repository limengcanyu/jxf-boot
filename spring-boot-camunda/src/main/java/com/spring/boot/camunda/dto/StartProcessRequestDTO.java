package com.spring.boot.camunda.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 启动流程请求DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class StartProcessRequestDTO {
    
    /**
     * 流程定义Key
     */
    @NotBlank(message = "流程定义Key不能为空")
    private String processDefinitionKey;
    
    /**
     * 业务Key
     */
    private String businessKey;
    
    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    // Constructors
    public StartProcessRequestDTO() {}

    public StartProcessRequestDTO(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        this.processDefinitionKey = processDefinitionKey;
        this.businessKey = businessKey;
        this.variables = variables;
    }

    // Getter and Setter methods
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "StartProcessRequestDTO{" +
                "processDefinitionKey='" + processDefinitionKey + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", variables=" + variables +
                '}';
    }
}