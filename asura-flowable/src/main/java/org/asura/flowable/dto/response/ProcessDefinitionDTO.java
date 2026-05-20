package org.asura.flowable.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProcessDefinitionDTO {

    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private Integer version;
    private String description;
    private String deploymentId;
    private String resourceName;
    private Boolean isActive;
    private Boolean isSuspended;
    private LocalDateTime deploymentTime;

}