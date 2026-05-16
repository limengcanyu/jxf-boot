package org.asura.activiti.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Setter
@Getter
public class TaskDTO {

    private String taskId;
    private String name;
    private String assignee;
    private String candidateGroup;
    private String processInstanceId;
    private String processDefinitionId;
    private Date createTime;
    private Date dueDate;
    private String description;
    private Map<String, Object> variables;

    public TaskDTO() {
    }

}