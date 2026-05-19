package org.asura.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ShutdownHookTest {

    @Test
    void testContextClosedEventHandling() {
        // 1. 手动启动 Spring Boot 应用，创建独立的上下文实例
        log.info("=== 手动启动 Spring 应用上下文 ===");
        ConfigurableApplicationContext context = SpringApplication.run(AsuraCompletablefutureApplication.class);

//        // 2. 从手动创建的上下文中获取监听器实例
//        VirtualThreadContextClosedListener listener = context.getBean(VirtualThreadContextClosedListener.class);

        // 4. 手动关闭上下文（触发 ContextClosedEvent）
        log.info("=== 手动关闭 Spring 应用上下文 ===");
        context.close();

        // 可选：验证上下文确实已关闭
        assertTrue(!context.isActive(), "上下文应处于非活跃状态");
    }
}
