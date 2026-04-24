package com.spring.boot.undertow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EchoControllerTests {

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void echo() {
        Random random = new Random();

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        for (int j = 0; j < 10000; j++) { // 循环1000次，放入10000个任务
            executorService.submit(() -> {
                System.out.println("thread id: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());

                for (int i = 0; i < 1000000; i++) {
                    testRestTemplate.getForObject("http://localhost:8071/echo", String.class);
                    System.out.println("调用 " + i + " 次");

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
