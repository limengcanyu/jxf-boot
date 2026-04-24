package org.akuma.activiti.dto;

/**
 * 流程定义响应DTO
 * 
 * @author Auto Generated
 * @version 1.0
 */
public class ProcessDefinitionDTO {

    private String id;
    private String key;
    private String name;
    private int version;
    private String deploymentId;
    private String resourceName;
    private boolean suspended;

    // 构造方法
    public ProcessDefinitionDTO() {
    }

    public ProcessDefinitionDTO(String id, String key, String name, int version,
                                String deploymentId, String resourceName, boolean suspended) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.version = version;
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.suspended = suspended;
    }

    // Getter和Setter方法
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
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
                '}';
    }
}