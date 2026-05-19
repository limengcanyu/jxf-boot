package org.asura.ssl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class AsuraSslApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 支持https调用
     */
    @Test
    void test() {
        String result = restTemplate.getForObject("https://localhost:8443/echo", String.class);
        System.out.println("result: " + result);
    }
}
