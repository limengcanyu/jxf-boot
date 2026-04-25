package org.asura.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableCaching
@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
public class AsuraTomcatApplication {
    private static final Logger logger = LoggerFactory.getLogger(AsuraTomcatApplication.class);

    public static void main(String[] args) {
//         // Disabling Restart
//        System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext context = SpringApplication.run(AsuraTomcatApplication.class);
        logger.debug("application started...");


    }
}
