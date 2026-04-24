package com.spring.boot.camunda.dto;

import java.util.Map;

/**
 * 完成任务请求DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class CompleteTaskRequestDTO {
    
    /**
     * 任务变量
     */
    private Map<String, Object> variables;
    
    /**
     * 评论
     */
    private String comment;

    // Constructors
    public CompleteTaskRequestDTO() {}

    public CompleteTaskRequestDTO(Map<String, Object> variables, String comment) {
        this.variables = variables;
        this.comment = comment;
    }

    // Getter and Setter methods
    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CompleteTaskRequestDTO{" +
                "variables=" + variables +
                ", comment='" + comment + '\'' +
                '}';
    }
}