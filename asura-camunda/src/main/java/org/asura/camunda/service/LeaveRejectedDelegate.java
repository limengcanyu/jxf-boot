package org.asura.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 请假拒绝委托类
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
@Component("leaveRejectedDelegate")
public class LeaveRejectedDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRejectedDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String applicant = (String) execution.getVariable("applicant");
        String leaveType = (String) execution.getVariable("leaveType");
        Integer days = (Integer) execution.getVariable("days");
        String reason = (String) execution.getVariable("reason");
        String rejectReason = (String) execution.getVariable("rejectReason");
        
        logger.info("处理请假拒绝：申请人={}, 请假类型={}, 天数={}, 申请原因={}, 拒绝原因={}", 
                   applicant, leaveType, days, reason, rejectReason);
        
        // 在实际项目中，这里可以：
        // 1. 发送拒绝通知邮件给申请人
        // 2. 更新HR系统中的请假记录状态
        // 3. 记录拒绝原因和审批日志
        // 4. 触发后续处理流程
        
        // 设置流程变量标记处理结果
        execution.setVariable("processResult", "REJECTED");
        execution.setVariable("processMessage", "请假申请已拒绝，原因：" + 
                             (rejectReason != null ? rejectReason : "未提供拒绝原因"));
        execution.setVariable("processTime", System.currentTimeMillis());
        
        logger.info("请假拒绝处理完成：申请人={}", applicant);
    }
}