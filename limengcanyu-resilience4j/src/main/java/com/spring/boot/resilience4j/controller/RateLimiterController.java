package com.spring.boot.resilience4j.controller;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/ratelimiter")
public class RateLimiterController {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    /**
     * http://localhost:8080/ratelimiter/limiter1
     *
     * @return
     */
    @RateLimiter(name = "backendA")
    @RequestMapping("/limiter1")
    public String limiter1() {
        return "limiter1 result";
    }

    /**
     * http://localhost:8080/ratelimiter/limiter1
     *
     * @return
     */
    @RateLimiter(name = "backendB")
    @RequestMapping("/limiter2")
    public String limiter2() {
        return "limiter2 result";
    }

    /**
     * http://localhost:8080/ratelimiter/limiter1
     *
     * @return
     */
    @RateLimiter(name = "backendC", fallbackMethod = "fallBackByRatelimiter")
    @RequestMapping("/limiter3")
    public String limiter3() {
        return "limiter3 result";
    }

    private String fallBackByRatelimiter(RequestNotPermitted e) {
        log.debug("限流器启动，拒绝访问限流资源 ......");

        io.github.resilience4j.ratelimiter.RateLimiter ratelimiterA = rateLimiterRegistry.rateLimiter("backendC");
        io.github.resilience4j.ratelimiter.RateLimiter.Metrics metrics = ratelimiterA.getMetrics();

        log.debug("方法限流中：metrics[ availablePermissions: {} , numberOfWaitingThreads: {} ]", metrics.getAvailablePermissions(), metrics.getNumberOfWaitingThreads());

        return "限流回调结果";
    }

}