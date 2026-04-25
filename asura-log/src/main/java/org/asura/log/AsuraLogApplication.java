package org.asura.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 启用定时任务（如果需要）
@SpringBootApplication
public class AsuraLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsuraLogApplication.class, args);
	}

}
