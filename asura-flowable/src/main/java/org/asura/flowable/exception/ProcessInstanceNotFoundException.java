package org.asura.flowable.exception;

public class ProcessInstanceNotFoundException extends RuntimeException {

    public ProcessInstanceNotFoundException(String processInstanceId) {
        super("流程实例不存在: " + processInstanceId);
    }

    public ProcessInstanceNotFoundException(String processInstanceId, Throwable cause) {
        super("流程实例不存在: " + processInstanceId, cause);
    }
}