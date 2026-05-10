package org.asura.code.executor;

import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.asura.code.executor.util.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class AsuraCodeExecutorApplication {

    public static void main(String[] args) {
        // 检查JDK环境
        if (!SystemUtils.isJdkEnvironment()) {
            log.error("❌ 当前运行环境为JRE，无编译能力，请切换至JDK运行！");
            System.exit(1);
        }

        // 启动应用
        SpringApplication.run(AsuraCodeExecutorApplication.class, args);
    }

    /**
     * 应用启动完成后执行
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("========================================");
        log.info("✅ Java代码上传运行系统启动成功！");
        log.info("📌 进程ID: {}", SystemUtils.getProcessId());
        log.info("🌐 访问地址: http://localhost:8080");
        log.info("🔧 接口文档: /api/java/execute/upload (文件上传)");
        log.info("🔧 接口文档: /api/java/execute/code (文本提交)");
        log.info("📊 监控指标: /actuator/prometheus");
        log.info("========================================");

        // 打印系统信息
        log.info("系统信息:\n{}", SystemUtils.getSystemInfo());

        // 记录启动指标
        Metrics.counter("java.code.application.start").increment();
    }

}
