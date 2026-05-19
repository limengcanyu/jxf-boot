package org.asura.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 请假批准委托类
 * 
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
@Component("leaveApprovedDelegate")
public class LeaveApprovedDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LeaveApprovedDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String applicant = (String) execution.getVariable("applicant");
        String leaveType = (String) execution.getVariable("leaveType");
        Integer days = (Integer) execution.getVariable("days");
        String reason = (String) execution.getVariable("reason");
        
        logger.info("处理请假批准：申请人={}, 请假类型={}, 天数={}, 原因={}", 
                   applicant, leaveType, days, reason);
        
        // 在实际项目中，这里可以：
        // 1. 发送批准通知邮件给申请人
        // 2. 更新HR系统中的请假记录
        // 3. 同步到考勤系统
        // 4. 记录审批日志
        
        // 设置流程变量标记处理结果
        execution.setVariable("processResult", "APPROVED");
        execution.setVariable("processMessage", "请假申请已批准");
        execution.setVariable("processTime", System.currentTimeMillis());
        
        logger.info("请假批准处理完成：申请人={}", applicant);
    }
}