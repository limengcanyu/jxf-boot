package com.spring.boot.rocketmq.acl.consume;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
public class SpringBootRocketmqAclConsumeApplication implements CommandLineRunner {

	@Resource
	private RocketMQTemplate rocketMQTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRocketmqAclConsumeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		////This is an example of pull consumer with access-key and secret-key.
		List<String> messages = rocketMQTemplate.receive(String.class);
		System.out.printf("receive from rocketMQTemplate, messages=%s %n", messages);
	}

}
