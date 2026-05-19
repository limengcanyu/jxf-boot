package org.asura.akka;

import org.asura.akka.config.TestAkkaConfig;
import org.asura.akka.controller.TestCounterController;
import org.asura.akka.service.TestCounterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 测试专用的独立应用入口
 * 
 * <p>完全独立于生产代码，不扫描任何集群相关类。
 * 只扫描测试专用的控制器和服务。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "org.asura.akka.service"
})
@Import({
    TestAkkaConfig.class,
    TestCounterController.class
})
public class TestAkkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAkkaApplication.class, args);
    }
}