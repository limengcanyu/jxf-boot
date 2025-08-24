package com.spring.boot.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 请假申请拒绝处理服务
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class LeaveRejectedService implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRejectedService.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String applicant = (String) execution.getVariable("applicant");
        String leaveType = (String) execution.getVariable("leaveType");
        Object startDate = execution.getVariable("startDate");
        Object endDate = execution.getVariable("endDate");
        Object days = execution.getVariable("days");
        String reason = (String) execution.getVariable("reason");
        
        // 获取拒绝原因
        String managerComment = (String) execution.getVariable("managerComment");
        String hrComment = (String) execution.getVariable("hrComment");
        String rejectReason = "";
        
        if (managerComment != null && !managerComment.isEmpty()) {
            rejectReason = "经理审批意见：" + managerComment;
        }
        if (hrComment != null && !hrComment.isEmpty()) {
            if (!rejectReason.isEmpty()) {
                rejectReason += "；";
            }
            rejectReason += "HR审批意见：" + hrComment;
        }

        logger.info("=== 请假申请已拒绝 ===");
        logger.info("申请人: {}", applicant);
        logger.info("请假类型: {}", leaveType);
        logger.info("开始日期: {}", startDate);
        logger.info("结束日期: {}", endDate);
        logger.info("请假天数: {}", days);
        logger.info("请假原因: {}", reason);
        logger.info("拒绝原因: {}", rejectReason);

        // 这里可以添加实际的业务逻辑，例如：
        // 1. 发送拒绝通知邮件给申请人
        // 2. 记录拒绝原因到系统
        // 3. 更新申请状态
        // 4. 记录到审批日志

        // 模拟发送拒绝通知
        sendRejectionNotification(applicant, "您的请假申请未通过审批", 
            String.format("请假申请（%s，%s至%s）未通过审批。拒绝原因：%s", 
                leaveType, startDate, endDate, rejectReason.isEmpty() ? "无具体原因" : rejectReason));

        // 设置结果变量
        execution.setVariable("finalResult", "REJECTED");
        execution.setVariable("rejectReason", rejectReason);
        execution.setVariable("processEndTime", new java.util.Date());

        logger.info("请假申请拒绝处理完成，流程实例ID: {}", execution.getProcessInstanceId());
    }

    /**
     * 发送拒绝通知（模拟）
     * 
     * @param recipient 接收人
     * @param subject 主题
     * @param content 内容
     */
    private void sendRejectionNotification(String recipient, String subject, String content) {
        logger.info("发送拒绝通知给 {}: {} - {}", recipient, subject, content);
        // 实际项目中可以集成邮件服务、短信服务或内部消息系统
    }
}