package org.asura.activiti.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Setter
@Getter
public class ProcessInstanceDTO {

    private String processInstanceId;
    private String processDefinitionId;
    private String processDefinitionName;
    private String businessKey;
    private String startUserId;
    private Date startTime;
    private Date endTime;
    private String status;
    private Map<String, Object> variables;

    public ProcessInstanceDTO() {
    }

}