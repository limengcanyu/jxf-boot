package com.spring.boot.sentinel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
class SpringBootSentinelApplicationTests {

	private final TestRestTemplate testRestTemplate = new TestRestTemplate();

	@Test
	void contextLoads() throws ExecutionException, InterruptedException {
		List<Future<String>> futureList = new ArrayList<>();

		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
		for (int i = 0; i < 10; i++) {
			Callable<String> callable = () -> {
				String result = testRestTemplate.getForObject("http://localhost:8080/hello/jessica", String.class);
				System.out.println(result);
				return result;
			};
			Future<String> future = threadPoolExecutor.submit(callable);
			futureList.add(future);
		}

//		try {
//			TimeUnit.MINUTES.sleep(5);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		for (Future<String> future : futureList) {
			System.out.println(future.get());
		}
	}

}
