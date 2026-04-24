package com.spring.boot.async.service.impl;

import com.spring.boot.async.service.SyncTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SyncTaskServiceImpl implements SyncTaskService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskWithoutResultServiceImpl.class);

    @Override
    public void task1() throws InterruptedException {
        logger.debug("service 执行同步任务 开始 线程 id: {} name: {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();

        Thread.sleep(3000);

        String result = "耗时: " + (System.currentTimeMillis() - currentTimeMillis) + "ms";
        logger.debug("service 执行同步任务 结束 线程 id: {} name: {} {}", Thread.currentThread().getId(), Thread.currentThread().getName(), result);
    }
}
