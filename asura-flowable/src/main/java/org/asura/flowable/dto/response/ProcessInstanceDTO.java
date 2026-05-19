package org.asura.flowable.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
public class ProcessInstanceDTO {

    private String processInstanceId;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private String businessKey;
    private String status;
    private String statusDesc;
    private String initiator;
    private Map<String, Object> variables;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}