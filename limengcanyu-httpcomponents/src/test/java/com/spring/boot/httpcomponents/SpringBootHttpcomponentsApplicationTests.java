package com.spring.boot.httpcomponents;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
class SpringBootHttpcomponentsApplicationTests {

    @Resource
    private RestTemplate httpRestTemplate;

    @Resource
    private RestTemplate httpsRestTemplate;

    @Test
    void httpRestTemplate() {
        String url = "http://localhost:8081/echo"; // 正确
//        String url = "https://localhost:9527/get/test?name=rock"; // 错误
        String result = httpRestTemplate.getForObject(url, String.class);
        System.out.println(result);
        Assertions.assertThat(result).isNotNull(); // 正确
//		Assertions.assertThat(result).isNull(); // 错误
    }

    @Test
    void httpsRestTemplate() {
        String url = "https://localhost:9527/get/test?name=rock";
        String result = httpsRestTemplate.getForObject(url, String.class);
        System.out.println(result);
        Assertions.assertThat(result).isNotNull(); // 正确
//		Assertions.assertThat(result).isNull(); // 错误
    }

}
