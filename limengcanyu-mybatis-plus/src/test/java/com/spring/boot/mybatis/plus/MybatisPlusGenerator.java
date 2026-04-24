package com.spring.boot.mybatis.plus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.242.129:3306/artanis?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "PassW0rd321";

        String author = "rock";

        String projectPath = System.getProperty("user.dir");
        String modulePath = "/spring-boot-mybatis-plus";
        String javaPath = projectPath + modulePath + "/src/main/java";
        String resourcePath = projectPath + modulePath + "/src/main/resources/mapper";

        String parent = "com.spring.boot.mybatis.plus";
        String include = "data_change_log";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(javaPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parent) // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, resourcePath)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(include) // 设置需要生成的表名
                            .addTablePrefix("") // 设置过滤表前缀
                            .controllerBuilder().enableRestStyle()
                            // Service文件名，去掉I前缀
                            .serviceBuilder().convertServiceFileName(entityName -> entityName + ConstVal.SERVICE)
                            .enableFileOverride()
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
