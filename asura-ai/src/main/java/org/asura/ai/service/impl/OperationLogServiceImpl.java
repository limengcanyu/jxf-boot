package org.asura.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.asura.ai.entity.OperationLog;
import org.asura.ai.mapper.OperationLogMapper;
import org.asura.ai.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void log(String userId, String username, String operation, String targetType, String targetId, String ipAddress) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperation(operation);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());
        operationLogMapper.insert(log);
    }

    @Override
    public List<OperationLog> getLogsByUserId(String userId) {
        return operationLogMapper.selectByUserId(userId);
    }

    @Override
    public List<OperationLog> getLogsByOperation(String operation) {
        return operationLogMapper.selectByOperation(operation);
    }

    @Override
    public List<OperationLog> getAllLogs(int page, int size) {
        Page<OperationLog> pageRequest = new Page<>(page, size);
        LambdaQueryWrapper<OperationLog> query = new LambdaQueryWrapper<>();
        query.orderByDesc(OperationLog::getTimestamp);
        IPage<OperationLog> result = operationLogMapper.selectPage(pageRequest, query);
        return result.getRecords();
    }
}
