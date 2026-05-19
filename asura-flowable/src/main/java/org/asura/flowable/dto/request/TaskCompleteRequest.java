package org.asura.flowable.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class TaskCompleteRequest {

    private String taskId;
    private String action;
    private String comment;
    private Map<String, Object> variables;

    public TaskCompleteRequest() {
    }

}