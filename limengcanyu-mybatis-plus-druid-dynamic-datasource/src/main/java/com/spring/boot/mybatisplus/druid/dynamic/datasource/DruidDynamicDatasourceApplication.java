package com.spring.boot.mybatisplus.druid.dynamic.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@MapperScan("com.spring.boot.mybatisplus.druid.dynamic.datasource.mapper")
@SpringBootApplication
public class DruidDynamicDatasourceApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DruidDynamicDatasourceApplication.class);

        // 获取容器中的Bean数量
        System.out.println("BeanDefinitionCount: " + ctx.getBeanDefinitionCount());

        // 获取容器中的Bean定义名称
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println("BeanDefinitionName: " + name);
        }

        // 获取容器中指定名称的Bean
        System.out.println(ctx.getBean("dataSource").getClass());
        // 获取容器中指定类型的Bean
        System.out.println(ctx.getBean(DruidDataSource.class));

        DruidDataSource dataSource = ctx.getBean(DruidDataSource.class);

        // 打印数据源的初始化栈
        String initStackTrace = dataSource.getInitStackTrace();
        System.out.println("initStackTrace: " + initStackTrace);
    }
}
