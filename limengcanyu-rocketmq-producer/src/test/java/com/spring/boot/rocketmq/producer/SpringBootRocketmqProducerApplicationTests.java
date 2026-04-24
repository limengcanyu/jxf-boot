package com.spring.boot.rocketmq.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootRocketmqProducerApplicationTests {

	@Autowired
	private RocketMQService rocketMQService;

	@Test
	void contextLoads() {
		rocketMQService.sendMessage();

//		for (int i = 0; i < 5; i++) {
//			rocketMQService.sendMessage();
//		}
	}

}
