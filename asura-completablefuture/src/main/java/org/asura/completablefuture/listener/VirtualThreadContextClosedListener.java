package org.asura.completablefuture.listener;

import lombok.extern.slf4j.Slf4j;
import org.asura.completablefuture.executor.VirtualThreadAsyncExecutor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * Spring Boot 关闭钩子（ApplicationListener 实现）
 * 适配 Spring Boot 3.5.8 + JDK 25
 */
@Slf4j
@Component
public class VirtualThreadContextClosedListener implements ApplicationListener<ContextClosedEvent> {

    /**
     * 监听 Spring 容器关闭事件（ContextClosedEvent）
     * 触发时机：应用停止（kill/ctrl+c/优雅停机）、容器关闭
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("=== 捕获Spring容器关闭事件[{}]，开始释放虚拟线程资源 ===", event.getTimestamp());
        // JDK 25 优化：虚拟线程中断时的资源清理
        Thread.currentThread().setName("virtual-thread-shutdown-hook");

        // 执行关闭逻辑
        shutdownExecutor();

        log.info("=== Spring容器关闭完成，虚拟线程资源释放完毕 ===");
    }

    /**
     * 封装关闭逻辑（便于复用）
     */
    private void shutdownExecutor() {
        try {
            VirtualThreadAsyncExecutor.shutdown();
        } catch (Exception e) {
            log.error("虚拟线程执行器关闭失败，执行强制关闭", e);
            try {
                // JDK 25 中 VirtualThread 的 shutdownNow() 更高效，支持批量中断
                if (VirtualThreadAsyncExecutor.VIRTUAL_THREAD_EXECUTOR instanceof ExecutorService executorService) {
                    // 等待3秒优雅关闭，超时则强制中断
                    if (!executorService.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
