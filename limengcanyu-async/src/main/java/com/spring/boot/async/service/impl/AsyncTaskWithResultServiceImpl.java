package com.spring.boot.async.service.impl;

import com.spring.boot.async.service.AsyncTaskWithResultOtherService;
import com.spring.boot.async.service.AsyncTaskWithResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class AsyncTaskWithResultServiceImpl implements AsyncTaskWithResultService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskWithResultServiceImpl.class);

    @Autowired
    private AsyncTaskWithResultOtherService asyncTaskWithResultOtherService;

    @Async
    @Override
    public Future<String> task1() throws InterruptedException {
        logger.debug("service 执行返回Future任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回Future任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task1执行完毕");
    }

    @Async
    @Override
    public Future<String> task2() throws InterruptedException {
        logger.debug("service 执行返回Future任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回Future任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task2执行完毕");
    }

    @Async
    @Override
    public Future<String> task3() throws InterruptedException {
        logger.debug("service 执行返回Future任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回Future任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task3执行完毕");
    }

    @Async
    @Override
    public ListenableFuture<String> task4() throws InterruptedException {
        logger.debug("service 执行返回ListenableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回ListenableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task4执行完毕");
    }

    @Async
    @Override
    public ListenableFuture<String> task5() throws InterruptedException {
        logger.debug("service 执行返回ListenableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回ListenableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task5执行完毕");
    }

    @Async
    @Override
    public ListenableFuture<String> task6() throws InterruptedException {
        logger.debug("service 执行返回ListenableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回ListenableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return new AsyncResult<>("task6执行完毕");
    }

    @Async
    @Override
    public CompletableFuture<String> task7() throws InterruptedException, ExecutionException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        long currentTimeMillis2 = System.currentTimeMillis();

        CompletableFuture<String> otherTask7 = asyncTaskWithResultOtherService.task7();
        CompletableFuture<String> otherTask8 = asyncTaskWithResultOtherService.task8();
        CompletableFuture<String> otherTask9 = asyncTaskWithResultOtherService.task9();

        // 本类中其它异步方法，不会异步执行
//        CompletableFuture<String> task8 = task8();

        // 等待每个异步调用都完成
        CompletableFuture.allOf(otherTask7, otherTask8, otherTask9).join();
        logger.debug("service 执行其它任务 结果 task7: {} task8: {} task9: {}", otherTask7.get(), otherTask8.get(), otherTask9.get());

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis2) + "ms";
        logger.debug("service 执行其它任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        // 使用结果返回一个已完成的CompletableFuture
        return CompletableFuture.completedFuture("task7执行完毕");
    }

    @Async
    @Override
    public CompletableFuture<String> task8() throws InterruptedException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(5000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return CompletableFuture.completedFuture("task8执行完毕");
    }

    @Async
    @Override
    public CompletableFuture<String> task9() throws InterruptedException {
        logger.debug("service 执行返回CompletableFuture任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(4500);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行返回CompletableFuture任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);

        return CompletableFuture.completedFuture("task9执行完毕");
    }
}
