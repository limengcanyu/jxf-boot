package com.spring.boot.async.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * description:
 *
 * @author rock
 * time 2020/7/3 0003 14:53
 */
public class VisibleThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(VisibleThreadPoolTaskExecutor.class);

    private void printThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        logger.info("{}, {}, taskCount: {}, completedTaskCount: {}, activeCount: {}, queueSize: {}",
                this.getThreadNamePrefix(),
                prefix,
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        printThreadPoolInfo("execute Runnable task");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        printThreadPoolInfo("execute Runnable task with startTimeout");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        printThreadPoolInfo("submit Runnable task");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        printThreadPoolInfo("submit Callable task");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        printThreadPoolInfo("submitListenable Runnable task");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        printThreadPoolInfo("submitListenable Callable task");
        return super.submitListenable(task);
    }
}
