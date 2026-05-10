package org.asura.completablefuture.executor;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 生产环境级别的 CompletableFuture + 虚拟线程工具类
 * 基于 Java 21+ 虚拟线程特性，提供异步任务编排能力
 */
@Slf4j
public class VirtualThreadAsyncExecutor {

    // 虚拟线程执行器（生产环境建议通过配置中心管控核心参数）
    public static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * 提交异步任务（基础方法）
     *
     * @param supplier 任务逻辑
     * @param <T>      返回值类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> submitAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(
                () -> {
                    String threadName = Thread.currentThread().getName();
                    log.info("虚拟线程[{}]开始执行任务", threadName);
                    try {
                        return supplier.get();
                    } catch (Exception e) {
                        log.error("虚拟线程[{}]执行任务异常", threadName, e);
                        log.error("任务执行失败");
//                        throw new RuntimeException("任务执行失败", e); // 改变了异常信息
                        throw new RuntimeException(e.getMessage()); // 保留原异常信息
                    } finally {
                        log.info("虚拟线程[{}]完成任务执行", threadName);
                    }
                },
                VIRTUAL_THREAD_EXECUTOR
        );
    }

    /**
     * 提交带超时的异步任务
     *
     * @param supplier 任务逻辑
     * @param timeout  超时时间
     * @param <T>      返回值类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> submitAsyncWithTimeout(Supplier<T> supplier, Duration timeout) {
        return submitAsync(supplier) // 先在这里处理异常
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionally(ex -> { // 再在这里处理异常
                    log.error("异步任务超时/异常，超时时间：{}ms", timeout.toMillis(), ex);
                    log.error("任务超时或执行失败");
//                    throw new RuntimeException("任务超时或执行失败", ex); // 改变了异常信息
                    throw new RuntimeException(ex.getCause().getMessage()); // 保留原异常信息
                });
    }

    /**
     * 并行执行多个任务（等待所有完成）
     *
     * @param suppliers 任务列表
     * @param <T>       返回值类型
     * @return CompletableFuture<List<T>>
     */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> executeAllAsync(Supplier<T>... suppliers) {
        List<CompletableFuture<T>> futures = Stream.of(suppliers)
                .map(VirtualThreadAsyncExecutor::submitAsync)
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    /**
     * 并行执行多个任务（取第一个完成的结果）
     *
     * @param suppliers 任务列表
     * @param <T>       返回值类型
     * @return CompletableFuture<T>
     */
    @SafeVarargs
    public static <T> CompletableFuture<T> executeAnyAsync(Supplier<T>... suppliers) {
        List<CompletableFuture<T>> futures = Stream.of(suppliers)
                .map(VirtualThreadAsyncExecutor::submitAsync)
                .toList();

        return CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(result -> (T) result);
    }

    /**
     * 并行执行多个任务（取第一个完成的结果）
     *
     * @param suppliers 任务列表
     * @param <T>       返回值类型
     * @return CompletableFuture<T>
     */
    @SafeVarargs
    public static <T> CompletableFuture<T> executeAnyAsync(Class<T> resultType, Supplier<T>... suppliers) {
        List<CompletableFuture<T>> futures = Stream.of(suppliers)
                .map(VirtualThreadAsyncExecutor::submitAsync)
                .toList();

        return CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(rawResult -> {
                    T result = null;
                    if (rawResult != null) {
                        if (resultType.isInstance(rawResult)) {
                            // 使用 Class.cast() 安全转换，替代手动 (T) 强制转换
                            result = resultType.cast(rawResult);
                        } else {
                            log.error("任意任务结果类型不匹配：期望 {}，实际 {}", resultType.getName(), rawResult.getClass().getName());
                        }
                    }
                    return result;
                });
    }

    /**
     * 任务编排：串行执行（任务B依赖任务A的结果）
     *
     * @param taskA 前置任务
     * @param taskB 后置任务（依赖taskA结果）
     * @param <A>   taskA返回值类型
     * @param <B>   taskB返回值类型
     * @return CompletableFuture<B>
     */
    public static <A, B> CompletableFuture<B> executeSerialAsync(Supplier<A> taskA, Function<A, B> taskB) {
        return submitAsync(taskA)
                .thenApplyAsync(taskB, VIRTUAL_THREAD_EXECUTOR)
                .exceptionally(ex -> {
                    log.error("串行任务执行异常", ex);
                    throw new RuntimeException("串行任务执行失败", ex);
                });
    }

    /**
     * 关闭执行器（应用关闭时调用）
     */
    public static void shutdown() {
        if (VIRTUAL_THREAD_EXECUTOR instanceof java.util.concurrent.ExecutorService executorService) {
            executorService.shutdown();
            try {
                // JDK 25 推荐：缩短等待时间（虚拟线程关闭更快）
                if (!executorService.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                // JDK 25 优化：保留中断状态，符合线程规范
                Thread.currentThread().interrupt();
            }
            log.info("虚拟线程执行器已关闭");
        }
    }

}
