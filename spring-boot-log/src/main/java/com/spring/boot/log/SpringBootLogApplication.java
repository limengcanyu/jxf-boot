package com.spring.boot.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 启用定时任务（如果需要）
@SpringBootApplication
public class SpringBootLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLogApplication.class, args);
	}

}
