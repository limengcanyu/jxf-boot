package org.asura.flowable.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskQueryRequest {

    private String taskName;
    private String taskKey;
    private String processInstanceId;
    private String processDefinitionKey;
    private String businessKey;
    private String assignee;
    private String candidateUser;
    private String candidateGroup;
    private Boolean claimed;
    private String processStatus;

    public TaskQueryRequest() {
    }

}