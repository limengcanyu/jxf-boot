package com.spring.boot.async.service.impl;

import com.spring.boot.async.service.AsyncTaskWithResultOtherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTaskWithResultOtherServiceImpl implements AsyncTaskWithResultOtherService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskWithResultOtherServiceImpl.class);

    @Async
    @Override
    public CompletableFuture<String> task7() throws InterruptedException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(1000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        // 使用结果返回一个已完成的CompletableFuture
        return CompletableFuture.completedFuture("task7执行完毕");
    }

    @Async
    @Override
    public CompletableFuture<String> task8() throws InterruptedException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(2000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return CompletableFuture.completedFuture("task8执行完毕");
    }

    @Async
    @Override
    public CompletableFuture<String> task9() throws InterruptedException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return CompletableFuture.completedFuture("task9执行完毕");
    }
}
