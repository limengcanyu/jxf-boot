package com.spring.boot.rocketmq.producer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootRocketmqProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRocketmqProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
