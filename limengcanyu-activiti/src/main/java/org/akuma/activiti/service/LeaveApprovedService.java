package org.akuma.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 请假申请通过处理服务
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class LeaveApprovedService implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LeaveApprovedService.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String applicant = (String) execution.getVariable("applicant");
        String leaveType = (String) execution.getVariable("leaveType");
        Object startDate = execution.getVariable("startDate");
        Object endDate = execution.getVariable("endDate");
        Object days = execution.getVariable("days");
        String reason = (String) execution.getVariable("reason");

        logger.info("=== 请假申请已通过 ===");
        logger.info("申请人: {}", applicant);
        logger.info("请假类型: {}", leaveType);
        logger.info("开始日期: {}", startDate);
        logger.info("结束日期: {}", endDate);
        logger.info("请假天数: {}", days);
        logger.info("请假原因: {}", reason);

        // 这里可以添加实际的业务逻辑，例如：
        // 1. 发送通知邮件给申请人
        // 2. 更新HR系统中的员工假期记录
        // 3. 同步到考勤系统
        // 4. 记录到审批日志

        // 模拟发送通知
        sendNotification(applicant, "您的请假申请已通过审批", 
            String.format("请假类型：%s，时间：%s至%s，天数：%s天", leaveType, startDate, endDate, days));

        // 设置结果变量
        execution.setVariable("finalResult", "APPROVED");
        execution.setVariable("processEndTime", new java.util.Date());

        logger.info("请假申请处理完成，流程实例ID: {}", execution.getProcessInstanceId());
    }

    /**
     * 发送通知（模拟）
     * 
     * @param recipient 接收人
     * @param subject 主题
     * @param content 内容
     */
    private void sendNotification(String recipient, String subject, String content) {
        logger.info("发送通知给 {}: {} - {}", recipient, subject, content);
        // 实际项目中可以集成邮件服务、短信服务或内部消息系统
    }
}