package org.asura.rocketmq.producer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsuraRocketmqProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AsuraRocketmqProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
