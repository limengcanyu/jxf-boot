package com.spring.boot.rate.limiting;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BusinessController {

    @Resource
    private ProductionRateLimiter rateLimiter;

    @GetMapping("/api/business")
    public String businessApi() {
        // 1. 限流拦截（放在业务逻辑最前面）
        if (!rateLimiter.tryAcquire()) {
            log.error("系统繁忙，请稍后重试");
            return "系统繁忙，请稍后重试";
        }

        // 2. 正常业务逻辑
        try {
            log.info("开始处理业务");
            return "业务处理成功";
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return "服务器内部错误";
        }
    }
}

