package org.asura.completablefuture;

import org.asura.completablefuture.executor.PlatformThreadAsyncExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CompletableFuture 生产实践单元测试
 */
class PlatformThreadAsyncExecutorTest {

    // 测试前初始化（无特殊操作）
    @BeforeAll
    static void setUp() {
        System.out.println("测试开始，初始化完成");
    }

    // 测试后关闭线程池
    @AfterAll
    static void tearDown() {
        PlatformThreadAsyncExecutor.shutdownExecutors();
        System.out.println("测试结束，线程池已关闭");
    }

    // ========== 测试单任务异步执行 ==========
    @Test
    @Timeout(1) // 测试整体超时
    void testExecuteIoIntensiveTask_success() {
        // 正常任务：返回固定值
        String result = PlatformThreadAsyncExecutor.executeIoIntensiveTask(
                () -> "IO Task Success",
                500, TimeUnit.MILLISECONDS
        );
        assertEquals("IO Task Success", result);
    }

    @Test
    @Timeout(1)
    void testExecuteIoIntensiveTask_timeout() {
        // 超时任务：休眠 1 秒，超时设置 500 毫秒
        String result = PlatformThreadAsyncExecutor.executeIoIntensiveTask(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "Timeout Task";
                },
                500, TimeUnit.MILLISECONDS
        );
        assertNull(result); // 超时返回 null
    }

    @Test
    @Timeout(1)
    void testExecuteIoIntensiveTask_exception() {
        // 异常任务：主动抛出异常
        String result = PlatformThreadAsyncExecutor.executeIoIntensiveTask(
                () -> {
                    throw new RuntimeException("Task Exception");
                },
                500, TimeUnit.MILLISECONDS
        );
        assertNull(result); // 异常返回 null
    }

    // ========== 测试串行任务 ==========
    @Test
    @Timeout(1)
    void testExecuteSerialTasks_success() {
        // 任务1：返回数字 10
        // 任务2：将数字乘以 2
        Integer result = PlatformThreadAsyncExecutor.executeSerialTasks(
                () -> 10,
                num -> num * 2,
                500, TimeUnit.MILLISECONDS
        );
        assertEquals(20, result);
    }

    // ========== 测试并行任务（allOf） ==========
    @Test
    @Timeout(1)
    void testExecuteParallelTasks_success() {
        List<Supplier<Integer>> tasks = Arrays.asList(
                () -> 1,
                () -> 2,
                () -> 3
        );
        List<Integer> results = PlatformThreadAsyncExecutor.executeParallelTasks(
                tasks,
                500, TimeUnit.MILLISECONDS
        );
        assertNotNull(results);
        assertEquals(3, results.size());
        assertTrue(results.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    @Timeout(1)
    void testExecuteParallelTasks_partialException() {
        List<Supplier<Integer>> tasks = Arrays.asList(
                () -> 1,
                () -> { throw new RuntimeException("Task 2 Exception"); },
                () -> 3
        );
        List<Integer> results = PlatformThreadAsyncExecutor.executeParallelTasks(
                tasks,
                500, TimeUnit.MILLISECONDS
        );
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals(1, results.get(0));
        assertNull(results.get(1)); // 异常任务返回 null
        assertEquals(3, results.get(2));
    }

    // ========== 测试任意任务（anyOf） ==========
    @Test
    @Timeout(1)
    void testExecuteAnyOfTasks_success() {
        List<Supplier<String>> tasks = Arrays.asList(
                () -> {
                    try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return "Task 1";
                },
                () -> {
                    try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return "Task 2"; // 最快完成
                },
                () -> {
                    try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return "Task 3";
                }
        );
//        String result = CompletableFutureProductionUtils.executeAnyOfTasks(
//                tasks,
//                500, TimeUnit.MILLISECONDS
//        );

        String result = PlatformThreadAsyncExecutor.executeAnyOfTasks(
                tasks,
                String.class,
                500, TimeUnit.MILLISECONDS
        );

        assertEquals("Task 2", result);
    }

    // ========== 测试异步回调 ==========
    @Test
    @Timeout(1)
    void testExecuteTaskWithCallback_success() throws InterruptedException {
        // 测试成功回调
        StringBuilder callbackResult = new StringBuilder();
        PlatformThreadAsyncExecutor.executeTaskWithCallback(
                () -> "Callback Success",
                result -> callbackResult.append(result),
                ex -> callbackResult.append("Error")
        );
        // 等待回调执行（异步回调需要短暂等待）
        Thread.sleep(200);
        assertEquals("Callback Success", callbackResult.toString());
    }

    @Test
    @Timeout(1)
    void testExecuteTaskWithCallback_error() throws InterruptedException {
        // 测试异常回调
        StringBuilder callbackResult = new StringBuilder();
        PlatformThreadAsyncExecutor.executeTaskWithCallback(
                () -> { throw new RuntimeException("Callback Error"); },
                result -> callbackResult.append(result),
                ex -> callbackResult.append("Error: ").append(ex.getMessage())
        );
        Thread.sleep(200);
        assertEquals("Error: java.lang.RuntimeException: Callback Error", callbackResult.toString());
    }
}
