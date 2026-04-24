package com.spring.boot.resilience4j.controller;

import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/timelimiter")
@RestController
public class TimelimiterController {

    @Autowired
    private TimeLimiterRegistry timeLimiterRegistry;

    /**
     * http://localhost:8080/timelimiter/backendB
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/backendB")
    @TimeLimiter(name = "backendB", fallbackMethod = "timelimiterFallBack")
    public CompletableFuture<String> backendB() throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "异步超时器 正常结果";
        });
    }

    private CompletableFuture<String> timelimiterFallBack(Exception e) {
        log.debug("异步超时器启动，返回回调结果 ......", e);
        return CompletableFuture.supplyAsync(() -> "异步超时器 回调结果");
    }

}
