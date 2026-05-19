package org.asura.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


//@EnableScheduling
@EnableAsync
@SpringBootApplication
public class AsuraAsyncApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AsuraAsyncApplication.class, args);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (beanDefinitionName.toLowerCase().contains("task")) {
                System.out.println("beanDefinitionName: " + beanDefinitionName);
            }
        }
    }

}
