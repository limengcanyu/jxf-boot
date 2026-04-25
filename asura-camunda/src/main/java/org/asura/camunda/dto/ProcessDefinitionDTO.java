package org.asura.camunda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 流程定义响应DTO
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
public class ProcessDefinitionDTO {
    
    /**
     * 流程定义ID
     */
    private String id;
    
    /**
     * 流程定义Key
     */
    private String key;
    
    /**
     * 流程名称
     */
    private String name;
    
    /**
     * 版本
     */
    private Integer version;
    
    /**
     * 部署ID
     */
    private String deploymentId;
    
    /**
     * 资源名称
     */
    private String resourceName;
    
    /**
     * 是否暂停
     */
    private Boolean suspended;
    
    /**
     * 部署时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deploymentTime;

    // Constructors
    public ProcessDefinitionDTO() {}

    public ProcessDefinitionDTO(String id, String key, String name, Integer version) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.version = version;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    @Override
    public String toString() {
        return "ProcessDefinitionDTO{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", deploymentId='" + deploymentId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", suspended=" + suspended +
                ", deploymentTime=" + deploymentTime +
                '}';
    }
}