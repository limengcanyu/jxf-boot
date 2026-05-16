package org.asura.activiti.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessDefinitionDTO {

    private String id;
    private String name;
    private String key;
    private Integer version;
    private String resourceName;
    private String diagramResourceName;
    private boolean suspended;

    public ProcessDefinitionDTO() {
    }

}