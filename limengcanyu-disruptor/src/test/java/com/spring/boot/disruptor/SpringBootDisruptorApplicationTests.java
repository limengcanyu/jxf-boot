package com.spring.boot.disruptor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SpringBootDisruptorApplicationTests {

	@Autowired
	private DisruptorMqService disruptorMqService;

	/**
	 * 项目内部使用Disruptor做消息队列
	 * @throws Exception
	 */
	@Test
	public void sayHelloMqTest() throws Exception{
		disruptorMqService.publishMessage("消息到了，Hello world!");

		log.info("消息队列已发送完毕");

		// 这里停止2000ms是为了确定是处理消息是异步的
		Thread.sleep(2000);
	}

}
