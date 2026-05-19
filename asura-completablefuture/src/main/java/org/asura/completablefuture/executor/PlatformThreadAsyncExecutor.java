package org.asura.completablefuture.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 生产环境 CompletableFuture 最佳实践工具类
 * 核心原则：
 * 1. 禁用默认 ForkJoinPool，使用自定义线程池隔离业务
 * 2. 所有异步任务必须加超时控制
 * 3. 强制捕获异常，避免静默失败
 * 4. 线程池命名规范，便于问题排查
 */
public class PlatformThreadAsyncExecutor {
    private static final Logger log = LoggerFactory.getLogger(PlatformThreadAsyncExecutor.class);

    // ========== 1. 自定义线程池（生产环境核心） ==========
    // IO 密集型线程池（如调用外部接口、数据库查询）
    private static final ExecutorService IO_INTENSIVE_EXECUTOR = new ThreadPoolExecutor(
            10, // 核心线程数
            50, // 最大线程数
            60L, TimeUnit.SECONDS, // 空闲线程存活时间
            new LinkedBlockingQueue<>(1000), // 任务队列
            new ThreadFactoryBuilder().setNameFormat("io-intensive-pool-%d").build(), // 命名线程
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者线程执行（避免任务丢失）
    );

    // CPU 密集型线程池（如计算、数据处理）
    private static final ExecutorService CPU_INTENSIVE_EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), // 核心线程数 = CPU 核心数
            Runtime.getRuntime().availableProcessors() * 2, // 最大线程数
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("cpu-intensive-pool-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    // 关闭线程池（应用关闭时调用）
    public static void shutdownExecutors() {
        shutdownExecutor(IO_INTENSIVE_EXECUTOR);
        shutdownExecutor(CPU_INTENSIVE_EXECUTOR);
    }

    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    log.error("线程池关闭超时：{}", executor);
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // ========== 2. 单任务异步执行（带超时+异常处理） ==========
    /**
     * 执行单个异步任务（IO 密集型）
     * @param supplier 任务逻辑
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param <T> 返回值类型
     * @return 任务结果
     */
    public static <T> T executeIoIntensiveTask(Supplier<T> supplier, long timeout, TimeUnit timeUnit) {
        return executeTask(supplier, IO_INTENSIVE_EXECUTOR, timeout, timeUnit);
    }

    /**
     * 执行单个异步任务（CPU 密集型）
     */
    public static <T> T executeCpuIntensiveTask(Supplier<T> supplier, long timeout, TimeUnit timeUnit) {
        return executeTask(supplier, CPU_INTENSIVE_EXECUTOR, timeout, timeUnit);
    }

    private static <T> T executeTask(Supplier<T> supplier, ExecutorService executor, long timeout, TimeUnit timeUnit) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, executor)
                // 异常处理：捕获任务执行异常
                .exceptionally(ex -> {
                    log.error("异步任务执行异常", ex);
                    return null; // 可根据业务返回兜底值
                });

        try {
            // 超时控制：核心生产要求，避免任务挂起
            return future.get(timeout, timeUnit);
        } catch (TimeoutException e) {
            log.error("异步任务超时（超时时间：{} {}）", timeout, timeUnit);
            future.cancel(true); // 超时取消任务
            return null;
        } catch (InterruptedException e) {
            log.error("异步任务被中断", e);
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            log.error("异步任务执行失败", e.getCause());
            return null;
        }
    }

    // ========== 3. 多任务串行执行（依赖前置结果） ==========
    /**
     * 串行执行两个异步任务（任务2依赖任务1的结果）
     */
    public static <T, R> R executeSerialTasks(Supplier<T> task1, Function<T, R> task2, long timeout, TimeUnit timeUnit) {
        CompletableFuture<R> future = CompletableFuture.supplyAsync(task1, IO_INTENSIVE_EXECUTOR)
                // thenApply：同步处理前序结果（使用前序线程）
                // thenCompose：异步处理前序结果（返回新的 CompletableFuture）
                .thenCompose(result1 -> CompletableFuture.supplyAsync(() -> task2.apply(result1), IO_INTENSIVE_EXECUTOR))
                .exceptionally(ex -> {
                    log.error("串行任务执行异常", ex);
                    return null;
                });

        try {
            return future.get(timeout, timeUnit);
        } catch (Exception e) {
            log.error("串行任务超时/失败", e);
            future.cancel(true);
            return null;
        }
    }

    // ========== 4. 多任务并行执行（allOf：全部完成；anyOf：任一完成） ==========
    /**
     * 并行执行多个任务，等待全部完成后聚合结果
     */
    public static <T> List<T> executeParallelTasks(List<Supplier<T>> tasks, long timeout, TimeUnit timeUnit) {
        // 步骤1：创建多个异步任务（此时任务已经开始执行了！）
        // 将所有任务转换为 CompletableFuture
        List<CompletableFuture<T>> futureList = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, IO_INTENSIVE_EXECUTOR)
                        .exceptionally(ex -> {
                            log.error("并行任务执行异常", ex);
                            return null; // 单个任务失败不影响整体
                        }))
                .toList();

        // 步骤2：创建“等待信号”（allFutures）—— 仅创建对象，不阻塞
        // 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futureList.toArray(new CompletableFuture[0])
        );
        // 【关键】执行到这里时，子任务还在异步执行，代码不会停在这里等


        // 两种等待方式的关系：
        // 如果你先调用 allFutures.get(timeout)，那么后续调用 join() 时，子任务已经完成，join() 会立即返回结果（无阻塞）；
        // 如果你直接调用 join()，则会逐个等待子任务完成（本质还是等所有任务，和 allFutures.get() 效果一致）；
        // 生产环境推荐先调用 allFutures.get(timeout)（加超时控制），再调用 join()，避免 join() 无限制阻塞。
        try {
            // 步骤3：真正的等待操作（二选一）
            // 方式1：显式等待信号完成（推荐，可加超时）
            // 等待全部完成并获取结果
            allFutures.get(timeout, timeUnit);

            // 方式2：隐式等待每个任务结果（join会阻塞直到对应任务完成）
            return futureList.stream()
                    .map(CompletableFuture::join) // join 不会抛出检查异常 这里才是真正等每个任务完成
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("并行任务超时/失败", e);
            futureList.forEach(future -> future.cancel(true));
            return null;
        }
    }

    /**
     * 并行执行多个任务，返回第一个完成的结果（快速失败/快速返回）
     */
    public static <T> T executeAnyOfTasks(List<Supplier<T>> tasks, long timeout, TimeUnit timeUnit) {
        CompletableFuture<?>[] futureArray = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, IO_INTENSIVE_EXECUTOR)
                        .exceptionally(ex -> {
                            log.error("任意任务执行异常", ex);
                            return null;
                        }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(futureArray);

        try {
            Object result = anyFuture.get(timeout, timeUnit);
            // 取消其他未完成的任务
            for (CompletableFuture<?> future : futureArray) {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
            return (T) result;
        } catch (Exception e) {
            log.error("任意任务超时/失败", e);
            for (CompletableFuture<?> future : futureArray) {
                future.cancel(true);
            }
            return null;
        }
    }

    public static <T> T executeAnyOfTasks(List<Supplier<T>> tasks, Class<T> resultType, long timeout, TimeUnit timeUnit) {
        if (resultType == null) {
            log.warn("执行任意任务失败：结果类型不能为空");
            return null;
        }

        CompletableFuture<?>[] futureArray = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, IO_INTENSIVE_EXECUTOR)
                        .exceptionally(ex -> {
                            log.error("任意任务执行异常", ex);
                            return null;
                        }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(futureArray);

        try {
            Object rawResult = anyFuture.get(timeout, timeUnit);

            // 5. 安全转换类型（核心改进：消除 Unchecked cast 警告）
            T result = null;
            if (rawResult != null) {
                if (resultType.isInstance(rawResult)) {
                    // 使用 Class.cast() 安全转换，替代手动 (T) 强制转换
                    result = resultType.cast(rawResult);
                } else {
                    log.error("任意任务结果类型不匹配：期望 {}，实际 {}", resultType.getName(), rawResult.getClass().getName());
                }
            }

            // 取消其他未完成的任务
            for (CompletableFuture<?> future : futureArray) {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("任意任务超时/失败", e);
            for (CompletableFuture<?> future : futureArray) {
                future.cancel(true);
            }
            return null;
        }
    }


    /**
     * 安全获取 anyOf 的结果，消除 Unchecked cast 警告
     * @param type 类型令牌，指定 T 的具体类型
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 第一个完成的任务结果
     */
    public static <T> T getAnyOfResult(CompletableFuture<Object> anyFuture, Class<T> type, long timeout, TimeUnit unit) {
        try {
            Object result = anyFuture.get(timeout, unit);
            // 先检查类型，再转换，避免运行时异常
            if (type.isInstance(result)) {
                return type.cast(result); // 安全转换：Class.cast() 替代手动 (T)
            } else {
                log.error("结果类型不匹配，期望：{}，实际：{}", type, result.getClass());
                return null;
            }
        } catch (Exception e) {
            log.error("获取 anyOf 结果失败", e);
            return null;
        }
    }

    // ========== 5. 异步回调（无返回值） ==========
    /**
     * 异步执行任务后回调（无返回值，不阻塞主线程）
     */
    public static <T> void executeTaskWithCallback(Supplier<T> task, Consumer<T> successCallback, Consumer<Throwable> errorCallback) {
        CompletableFuture.supplyAsync(task, IO_INTENSIVE_EXECUTOR)
                // 成功回调
                .thenAccept(successCallback)
                // 异常回调
                .exceptionally(ex -> {
                    errorCallback.accept(ex);
                    return null;
                });
    }
}
