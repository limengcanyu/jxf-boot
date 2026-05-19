package org.asura.flowable.exception;

public class ProcessDefinitionNotFoundException extends RuntimeException {

    public ProcessDefinitionNotFoundException(String processDefinitionId) {
        super("流程定义不存在: " + processDefinitionId);
    }

    public ProcessDefinitionNotFoundException(String processDefinitionId, Throwable cause) {
        super("流程定义不存在: " + processDefinitionId, cause);
    }
}