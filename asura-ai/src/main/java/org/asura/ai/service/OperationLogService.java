package org.asura.ai.service;

import org.asura.ai.entity.OperationLog;

import java.util.List;

public interface OperationLogService {
    void log(String userId, String username, String operation, String targetType, String targetId, String ipAddress);
    List<OperationLog> getLogsByUserId(String userId);
    List<OperationLog> getLogsByOperation(String operation);
    List<OperationLog> getAllLogs(int page, int size);
}
