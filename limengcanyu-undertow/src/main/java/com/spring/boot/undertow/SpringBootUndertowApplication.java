package com.spring.boot.undertow;

import com.spring.boot.undertow.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class SpringBootUndertowApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootUndertowApplication.class);

        // 默认没有加载Controller，Service，使用的时候才加载
        // 注释掉以后，启动后没有这两个Bean的信息，未注释时，启动后有这两个Bean的信息，因为此处使用了这两个Bean
//        EchoController echoControllerBean = applicationContext.getBean(EchoController.class);
//        OrderService orderServiceBean = applicationContext.getBean(OrderService.class);
//        log.debug("echoControllerBean = {} orderServiceBean = {}", echoControllerBean, orderServiceBean);

//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//
//        for (String name : beanDefinitionNames) {
//            log.debug("beanName: {}", name);
//        }
    }
}
