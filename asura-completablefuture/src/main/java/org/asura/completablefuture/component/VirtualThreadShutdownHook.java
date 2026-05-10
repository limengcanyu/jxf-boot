package org.asura.completablefuture.component;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.asura.completablefuture.executor.VirtualThreadAsyncExecutor;

/**
 * Spring Boot 关闭钩子（@PreDestroy 实现）
 * 适配 Spring Boot 3.5.8 + JDK 25
 */
@Slf4j
//@Component // 必须交给Spring容器管理，否则@PreDestroy不生效
public class VirtualThreadShutdownHook {

    /**
     * 应用关闭时执行（销毁Bean前调用）
     * Spring Boot 3.x 中使用 jakarta.annotation.PreDestroy（而非javax）
     */
    @PreDestroy
    public void shutdownVirtualThreadExecutor() {
        log.info("=== 应用开始关闭，释放虚拟线程执行器资源 ===");
        try {
            // 调用工具类的关闭方法
            VirtualThreadAsyncExecutor.shutdown();
            log.info("=== 虚拟线程执行器资源释放完成 ===");
        } catch (Exception e) {
            log.error("=== 释放虚拟线程执行器资源失败 ===", e);
            // 兜底：强制关闭（JDK 25 新增的虚拟线程中断优化）
            if (VirtualThreadAsyncExecutor.VIRTUAL_THREAD_EXECUTOR instanceof java.util.concurrent.ExecutorService executorService) {
                executorService.shutdownNow();
            }
        }
    }
}
