package org.asura.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.asura.completablefuture.executor.VirtualThreadAsyncExecutor;
import org.asura.completablefuture.service.VirtualThreadBusinessService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CompletableFuture + 虚拟线程 生产级单元测试
 * 覆盖所有核心场景，确保生产环境可用
 */
@Slf4j
class VirtualThreadAsyncExecutorTest {

    private static VirtualThreadBusinessService virtualThreadBusinessService;

    @BeforeAll
    static void setUp() {
        virtualThreadBusinessService = new VirtualThreadBusinessService();
    }

    @AfterAll
    static void tearDown() {
        // 关闭执行器，释放资源
        VirtualThreadAsyncExecutor.shutdown();
    }

    /**
     * 测试：基础异步任务执行
     */
    @Test
    @Timeout(1) // 超时控制（1秒）
    void testSubmitAsync() throws ExecutionException, InterruptedException {
        // 执行异步任务
        CompletableFuture<VirtualThreadBusinessService.UserInfo> future = VirtualThreadAsyncExecutor.submitAsync(
                () -> virtualThreadBusinessService.queryUserInfo(1001L)
        );

        // 验证结果
        VirtualThreadBusinessService.UserInfo userInfo = future.get();
        assertNotNull(userInfo);
        assertEquals(1001L, userInfo.getUserId());
        assertEquals("用户_1001", userInfo.getUserName());
    }

    /**
     * 测试：带超时的异步任务
     */
    @Test
    @Timeout(1)
    void testSubmitAsyncWithTimeout() {
        // 正常场景（超时时间足够）
        CompletableFuture<VirtualThreadBusinessService.UserInfo> normalFuture = VirtualThreadAsyncExecutor.submitAsyncWithTimeout(
                () -> virtualThreadBusinessService.queryUserInfo(1002L),
                Duration.ofMillis(500)
        );
        assertDoesNotThrow(() -> {
            VirtualThreadBusinessService.UserInfo userInfo = normalFuture.get();
            assertEquals(1002L, userInfo.getUserId());
        });

        // 超时场景（超时时间不足）
        CompletableFuture<VirtualThreadBusinessService.UserInfo> timeoutFuture = VirtualThreadAsyncExecutor.submitAsyncWithTimeout(
                () -> {
//                        return businessService.queryUserInfo(1003L);

                    throw new RuntimeException("查询用户信息执行异常");
                },
                Duration.ofMillis(50)
        );
//        assertThrows(ExecutionException.class, timeoutFuture::get);

        // 验证异常捕获
        ExecutionException exception = assertThrows(ExecutionException.class, timeoutFuture::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
//        assertEquals("风险校验不通过", exception.getCause().getMessage());
//        assertEquals("任务超时或执行失败", exception.getCause().getMessage()); // 改变了异常信息
        assertEquals("查询用户信息执行异常", exception.getCause().getMessage()); // 保留原异常信息

    }

    /**
     * 测试：并行执行所有任务（allOf）
     */
    @Test
    @Timeout(1)
    void testExecuteAllAsync() throws ExecutionException, InterruptedException {
        // 并行查询用户信息、订单、积分
        CompletableFuture<List<Object>> future = VirtualThreadAsyncExecutor.executeAllAsync(
                () -> virtualThreadBusinessService.queryUserInfo(1004L),
                () -> virtualThreadBusinessService.queryUserOrder(1004L),
                () -> virtualThreadBusinessService.queryUserPoints(1004L)
        );

        List<Object> results = future.get();
        assertEquals(3, results.size());

        // 验证各结果
        VirtualThreadBusinessService.UserInfo userInfo = (VirtualThreadBusinessService.UserInfo) results.get(0);
        VirtualThreadBusinessService.OrderInfo orderInfo = (VirtualThreadBusinessService.OrderInfo) results.get(1);
        Integer points = (Integer) results.get(2);

        assertEquals(1004L, userInfo.getUserId());
        assertEquals(1004L, orderInfo.getUserId());
        assertEquals(100400, points);
    }

    /**
     * 测试：并行执行取第一个结果（anyOf）
     */
    @Test
    @Timeout(1)
    void testExecuteAnyAsync() throws ExecutionException, InterruptedException {
        // 并行执行多个任务，取最快完成的
        CompletableFuture<Object> future = VirtualThreadAsyncExecutor.executeAnyAsync(
                () -> {
                    try {
                        Thread.sleep(200); // 慢任务
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return virtualThreadBusinessService.queryUserInfo(1005L);
                },
                () -> {
                    try {
                        Thread.sleep(50); // 快任务
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return virtualThreadBusinessService.queryUserPoints(1005L);
                }
        );

        Object result = future.get();
        // 验证结果是积分（快任务先完成）
        assertInstanceOf(Integer.class, result);
        assertEquals(100500, result);

        // 指定返回类型
        CompletableFuture<Object> future2 = VirtualThreadAsyncExecutor.executeAnyAsync(
                Object.class,
                () -> {
                    try {
                        Thread.sleep(200); // 慢任务
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return virtualThreadBusinessService.queryUserInfo(1005L);
                },
                () -> {
                    try {
                        Thread.sleep(50); // 快任务
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return virtualThreadBusinessService.queryUserPoints(1005L);
                }
        );

        Object result2 = future2.get();
        // 验证结果是积分（快任务先完成）
        assertInstanceOf(Integer.class, result2);
        assertEquals(100500, result2);
    }

    /**
     * 测试：串行任务编排（依赖前置结果）
     */
    @Test
    @Timeout(1)
    void testExecuteSerialAsync() throws ExecutionException, InterruptedException {
        // 先查用户信息，再根据用户ID查订单
        CompletableFuture<VirtualThreadBusinessService.OrderInfo> future = VirtualThreadAsyncExecutor.executeSerialAsync(
                () -> virtualThreadBusinessService.queryUserInfo(1006L), // 前置任务
                userInfo -> virtualThreadBusinessService.queryUserOrder(userInfo.getUserId()) // 后置任务
        );

        VirtualThreadBusinessService.OrderInfo orderInfo = future.get();
        assertEquals(1006L, orderInfo.getUserId());
    }

    /**
     * 测试：异常处理场景
     */
    @Test
    @Timeout(1)
    void testExceptionHandling() {
        // 模拟风险校验失败的场景
        CompletableFuture<Boolean> future = VirtualThreadAsyncExecutor.submitAsync(
                () -> {
                    Boolean result = virtualThreadBusinessService.riskCheck(1007L); // 1007是奇数，返回false
                    if (!result) {
                        throw new RuntimeException("风险校验不通过");
                    }
                    return result;
                }
        );

        // 验证异常捕获
        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("风险校验不通过", exception.getCause().getMessage());
//        assertEquals("任务执行失败", exception.getCause().getMessage());
    }
}
