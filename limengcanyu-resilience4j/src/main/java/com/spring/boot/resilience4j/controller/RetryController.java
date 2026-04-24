package com.spring.boot.resilience4j.controller;

import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpRetryException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequestMapping("/retry")
@RestController
public class RetryController {

    @Autowired
    private RetryRegistry retryRegistry;

    /**
     * http://localhost:8080/retry/backendA
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/backendA")
    @Retry(name = "backendA", fallbackMethod = "fallBackByRetry")
    public String backendA() throws Exception {
        log.debug("RetryController backendA ......");
        throw new TimeoutException();
    }

    // 降级处理 捕获异常与抛出的异常对应
    private String fallBackByRetry(TimeoutException e) {
        log.debug("重试控制器开启，重试访问资源 ......");

        io.github.resilience4j.retry.Retry bulkheadA = retryRegistry.retry("bulkheadA");
        io.github.resilience4j.retry.Retry.Metrics metrics = bulkheadA.getMetrics();

        log.debug("方法重试中：metrics[ " +
                        "numberOfFailedCallsWithoutRetryAttempt: {} , " +
                        "numberOfFailedCallsWithRetryAttempt: {} , " +
                        "numberOfSuccessfulCallsWithoutRetryAttempt: {} , " +
                        "numberOfSuccessfulCallsWithRetryAttempt: {} " +
                        "]",
                metrics.getNumberOfFailedCallsWithoutRetryAttempt(),
                metrics.getNumberOfFailedCallsWithRetryAttempt(),
                metrics.getNumberOfSuccessfulCallsWithoutRetryAttempt(),
                metrics.getNumberOfSuccessfulCallsWithRetryAttempt());

        return "retry fallBack result";
    }

}
