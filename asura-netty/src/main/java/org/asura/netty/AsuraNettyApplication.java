package org.asura.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AsuraNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraNettyApplication.class, args);
        log.info("应用启动完成 ......");
    }

}
