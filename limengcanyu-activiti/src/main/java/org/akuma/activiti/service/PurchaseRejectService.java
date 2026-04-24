package org.akuma.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 采购申请拒绝服务
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class PurchaseRejectService implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseRejectService.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String applicant = (String) execution.getVariable("applicant");
        String itemName = (String) execution.getVariable("itemName");
        Object quantity = execution.getVariable("quantity");
        Object totalAmount = execution.getVariable("totalAmount");
        String supplier = (String) execution.getVariable("supplier");
        
        // 获取拒绝原因
        String deptComment = (String) execution.getVariable("deptComment");
        String financeComment = (String) execution.getVariable("financeComment");
        String ceoComment = (String) execution.getVariable("ceoComment");
        
        String rejectReason = buildRejectReason(deptComment, financeComment, ceoComment);

        logger.info("=== 采购申请已拒绝 ===");
        logger.info("申请人: {}", applicant);
        logger.info("采购物品: {}", itemName);
        logger.info("数量: {}", quantity);
        logger.info("总金额: {}", totalAmount);
        logger.info("供应商: {}", supplier);
        logger.info("拒绝原因: {}", rejectReason);

        // 这里可以添加实际的业务逻辑，例如：
        // 1. 发送拒绝通知邮件给申请人
        // 2. 记录拒绝原因到系统
        // 3. 更新申请状态
        // 4. 记录到审批日志
        // 5. 通知相关部门

        // 发送拒绝通知
        sendRejectionNotification(applicant, "您的采购申请未通过审批", 
            String.format("您的采购申请（%s，数量：%s，金额：%s）未通过审批。拒绝原因：%s", 
                itemName, quantity, totalAmount, rejectReason.isEmpty() ? "无具体原因" : rejectReason));

        // 设置结果变量
        execution.setVariable("finalResult", "REJECTED");
        execution.setVariable("rejectReason", rejectReason);
        execution.setVariable("processEndTime", new java.util.Date());

        logger.info("采购申请拒绝处理完成，流程实例ID: {}", execution.getProcessInstanceId());
    }

    /**
     * 构建拒绝原因
     * 
     * @param deptComment 部门意见
     * @param financeComment 财务意见
     * @param ceoComment CEO意见
     * @return 拒绝原因
     */
    private String buildRejectReason(String deptComment, String financeComment, String ceoComment) {
        StringBuilder reason = new StringBuilder();
        
        if (deptComment != null && !deptComment.isEmpty()) {
            reason.append("部门审批意见：").append(deptComment);
        }
        
        if (financeComment != null && !financeComment.isEmpty()) {
            if (reason.length() > 0) {
                reason.append("；");
            }
            reason.append("财务审批意见：").append(financeComment);
        }
        
        if (ceoComment != null && !ceoComment.isEmpty()) {
            if (reason.length() > 0) {
                reason.append("；");
            }
            reason.append("总经理审批意见：").append(ceoComment);
        }
        
        return reason.toString();
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