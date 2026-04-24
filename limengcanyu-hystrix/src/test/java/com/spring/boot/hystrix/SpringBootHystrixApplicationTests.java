package com.spring.boot.hystrix;

import org.apache.http.client.utils.HttpClientUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class SpringBootHystrixApplicationTests {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void contextLoads() throws InterruptedException {
        ExecutorService executorService  = Executors.newFixedThreadPool(20);

        CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int i = 1; i <= 20; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    countDownLatch.await();
                    Integer result = restTemplate.getForObject("http://localhost:8080/queryById/" + finalI, Integer.class);
                    System.out.println("result: " + result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            countDownLatch.countDown();
        }

        // 让主线程等待
        Thread.currentThread().join();
    }

    @Test
    void testMergeQuery() throws InterruptedException {
        ExecutorService executorService  = Executors.newFixedThreadPool(20);

        CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int i = 1; i <= 20; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    countDownLatch.await();
                    Integer result = restTemplate.getForObject("http://localhost:8080/mergeQueryById/" + finalI, Integer.class);
                    System.out.println("result: " + result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            countDownLatch.countDown();
        }

        // 让主线程等待
        Thread.currentThread().join();
    }

}
