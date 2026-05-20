package org.asura.flowable.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String taskId) {
        super("任务不存在: " + taskId);
    }

    public TaskNotFoundException(String taskId, Throwable cause) {
        super("任务不存在: " + taskId, cause);
    }
}