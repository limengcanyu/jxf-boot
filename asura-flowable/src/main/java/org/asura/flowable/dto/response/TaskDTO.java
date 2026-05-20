package org.asura.flowable.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class TaskDTO {

    private String taskId;
    private String taskName;
    private String taskKey;
    private String description;
    private String processInstanceId;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private String businessKey;
    private String assignee;
    private List<String> candidateUsers;
    private List<String> candidateGroups;
    private LocalDateTime createTime;
    private LocalDateTime dueDate;
    private Integer priority;
    private Map<String, Object> variables;

}