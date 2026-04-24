package com.spring.boot.resilience4j.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RequestMapping("/circuitbreaker")
@RestController
public class CircuitbreakerController {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * http://localhost:8080/circuitbreaker/circuitBreakerNoFallbackMethod
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/circuitBreakerNoFallbackMethod")
    @CircuitBreaker(name = "backendA")
    public String circuitBreakerNoFallbackMethod() throws Exception {
        throw new Exception("服务异常1");
    }

    /**
     * http://localhost:8080/circuitbreaker/circuitBreakerWithFallbackMethod
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/circuitBreakerWithFallbackMethod")
    @CircuitBreaker(name = "backendB", fallbackMethod = "circuitBreakerFallBack")
    public String circuitBreakerWithFallbackMethod() throws Exception {
        int nextInt = new Random().nextInt(2);
        if (nextInt % 2 == 0) {
            throw new Exception("服务异常2");
        }
        return "正常结果";
    }

    private String circuitBreakerFallBack(CallNotPermittedException e) {
        log.debug("熔断器启动，拒绝访问资源 ......");

        io.github.resilience4j.circuitbreaker.CircuitBreaker order = circuitBreakerRegistry.circuitBreaker("circuitBreaker2");
        io.github.resilience4j.circuitbreaker.CircuitBreaker.Metrics metrics = order.getMetrics();

        log.debug("方法降级中：state: {} , metrics[ failureRate: {} , bufferedCalls: {} , failedCalls: {} , successCalls: {} , notPermittedCalls: {} ]",
                order.getState(), metrics.getFailureRate(), metrics.getNumberOfBufferedCalls(), metrics.getNumberOfFailedCalls(),
                metrics.getNumberOfSuccessfulCalls(), metrics.getNumberOfNotPermittedCalls());

        return "熔断结果";
    }

}
