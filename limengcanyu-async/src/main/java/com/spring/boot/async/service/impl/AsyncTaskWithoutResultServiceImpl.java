package com.spring.boot.async.service.impl;

import com.spring.boot.async.service.AsyncTaskWithoutResultService;
import com.spring.boot.async.utils.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncTaskWithoutResultServiceImpl implements AsyncTaskWithoutResultService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskWithoutResultServiceImpl.class);

    @Async
    @Override
    public void task1() throws InterruptedException {
        logger.debug("service 执行无返回任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行无返回任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);
    }

    @Async
    @Override
    public void task2() throws InterruptedException {
        logger.debug("service 执行无返回任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行无返回任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);
    }

    @Async
    @Override
    public void task3() throws InterruptedException {
        logger.debug("service 执行无返回任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行无返回任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);
    }

    @Async
//    @Async("asyncTaskExecutor") // 使用自定义线程池执行
    @Override
    public void task4(int count) {
        logger.debug("service 执行无返回任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("count: {} tenantId: {}", count, ContextUtils.getTenantId());

        logger.debug("service 执行无返回任务 结束 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }
}
