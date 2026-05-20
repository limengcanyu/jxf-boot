package org.asura.skywalking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SkyWalking集成示例应用启动类
 */
@Slf4j
@RestController
@SpringBootApplication
@RequestMapping("/")
public class AsuraSkywalkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraSkywalkingApplication.class, args);
        log.info("AsuraSkywalkingApplication started successfully");
    }

    /**
     * 健康检查接口
     *
     * @return 健康状态
     */
    @GetMapping("/hello")
    public String hello() {
        log.info("Received hello request");
        return "Hello, SkyWalking!";
    }

    /**
     * 健康检查接口
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public String health() {
        log.debug("Health check");
        return "OK";
    }

}