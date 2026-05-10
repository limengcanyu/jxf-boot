package org.asura.completablefuture.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 模拟生产环境业务服务
 */
public class VirtualThreadBusinessService {

    /**
     * 模拟：查询用户基础信息
     */
    public UserInfo queryUserInfo(Long userId) {
        // 模拟IO等待（虚拟线程擅长处理IO密集型任务）
        sleep(100);
        return new UserInfo(userId, "用户_" + userId, 25);
    }

    /**
     * 模拟：查询用户订单列表
     */
    public OrderInfo queryUserOrder(Long userId) {
        sleep(150);
        return new OrderInfo(userId, "订单_" + System.currentTimeMillis(), 99.9);
    }

    /**
     * 模拟：查询用户积分
     */
    public Integer queryUserPoints(Long userId) {
        sleep(80);
        return userId.intValue() * 100;
    }

    /**
     * 模拟：风险校验（可能超时/失败）
     */
    public Boolean riskCheck(Long userId) {
        sleep(200);
        // 模拟部分用户校验失败
        return userId % 2 == 0;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("任务被中断", e);
        }
    }

    // 数据模型
    @AllArgsConstructor
    @Data
    public static class UserInfo {
        private Long userId;
        private String userName;
        private Integer age;
    }

    @AllArgsConstructor
    @Data
    public static class OrderInfo {
        private Long userId;
        private String orderNo;
        private Double amount;
    }
}
