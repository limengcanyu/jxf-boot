package com.spring.boot.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 采购执行服务
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class PurchaseExecuteService implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseExecuteService.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String applicant = (String) execution.getVariable("applicant");
        String itemName = (String) execution.getVariable("itemName");
        Object quantity = execution.getVariable("quantity");
        Object unitPrice = execution.getVariable("unitPrice");
        Object totalAmount = execution.getVariable("totalAmount");
        String supplier = (String) execution.getVariable("supplier");
        String urgency = (String) execution.getVariable("urgency");
        String reason = (String) execution.getVariable("reason");

        logger.info("=== 开始执行采购 ===");
        logger.info("申请人: {}", applicant);
        logger.info("采购物品: {}", itemName);
        logger.info("数量: {}", quantity);
        logger.info("单价: {}", unitPrice);
        logger.info("总金额: {}", totalAmount);
        logger.info("供应商: {}", supplier);
        logger.info("紧急程度: {}", urgency);
        logger.info("采购理由: {}", reason);

        // 这里可以添加实际的业务逻辑，例如：
        // 1. 生成采购订单
        // 2. 联系供应商
        // 3. 安排付款流程
        // 4. 更新库存系统
        // 5. 发送通知给相关人员

        // 模拟生成采购订单号
        String purchaseOrderNo = generatePurchaseOrderNumber();
        execution.setVariable("purchaseOrderNo", purchaseOrderNo);

        // 模拟采购流程
        executePurchaseProcess(itemName, quantity, unitPrice, totalAmount, supplier, urgency);

        // 发送通知
        sendPurchaseNotification(applicant, "采购申请已通过并开始执行", 
            String.format("您的采购申请（%s）已通过审批，采购订单号：%s，将联系供应商%s进行采购。", 
                itemName, purchaseOrderNo, supplier));

        // 设置结果变量
        execution.setVariable("finalResult", "EXECUTED");
        execution.setVariable("processEndTime", new java.util.Date());

        logger.info("采购执行完成，采购订单号: {}，流程实例ID: {}", purchaseOrderNo, execution.getProcessInstanceId());
    }

    /**
     * 生成采购订单号
     * 
     * @return 采购订单号
     */
    private String generatePurchaseOrderNumber() {
        return "PO" + System.currentTimeMillis();
    }

    /**
     * 执行采购流程（模拟）
     * 
     * @param itemName 物品名称
     * @param quantity 数量
     * @param unitPrice 单价
     * @param totalAmount 总金额
     * @param supplier 供应商
     * @param urgency 紧急程度
     */
    private void executePurchaseProcess(String itemName, Object quantity, Object unitPrice, 
                                      Object totalAmount, String supplier, String urgency) {
        logger.info("正在执行采购流程...");
        
        // 模拟采购步骤
        logger.info("1. 生成采购合同");
        logger.info("2. 联系供应商: {}", supplier);
        logger.info("3. 确认交货时间");
        
        if ("high".equals(urgency)) {
            logger.info("4. 加急处理标记已设置");
        }
        
        logger.info("5. 安排付款流程，金额: {}", totalAmount);
        logger.info("6. 采购流程启动完成");
    }

    /**
     * 发送采购通知（模拟）
     * 
     * @param recipient 接收人
     * @param subject 主题
     * @param content 内容
     */
    private void sendPurchaseNotification(String recipient, String subject, String content) {
        logger.info("发送采购通知给 {}: {} - {}", recipient, subject, content);
        // 实际项目中可以集成邮件服务、短信服务或内部消息系统
    }
}