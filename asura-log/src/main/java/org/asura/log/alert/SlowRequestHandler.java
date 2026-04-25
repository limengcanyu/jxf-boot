package org.asura.log.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 慢请求处理器，可扩展为多种告警方式
 */
@Component
public class SlowRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(SlowRequestHandler.class);

    @EventListener
    public void handleSlowRequest(SlowRequestEvent event) {
        // 先将耗时格式化为保留2位小数的字符串
        String formattedCostMs = String.format("%.2f", event.getDurationMs());

        // 基础日志告警
        log.error("[慢请求告警] 接口:{} 耗时:{}ms IP:{} 参数:{}",
                event.getRequestKey(),
                formattedCostMs,
                event.getClientIp(),
                event.getParams());

        // 可扩展：
        // 1. 发送邮件通知：emailService.send("管理员", "慢请求告警", content)
        // 2. 企业微信/钉钉告警：messageService.sendToGroup(content)
        // 3. 写入告警系统：alertSystemClient.push(event)
    }
}

