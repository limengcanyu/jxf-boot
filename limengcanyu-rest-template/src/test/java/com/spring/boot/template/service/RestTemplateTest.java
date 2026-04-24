package com.spring.boot.template.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RestTemplateTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        String url = "/template/getRequestParam?id={id}" ; // 请求参数占位符

        Map<String, Object> uriVariables = new HashMap<>(); // 请求参数
        uriVariables.put("id", 1);

        Map map = restTemplate.getForObject(url, Map.class, uriVariables);
        System.out.println(map);
    }

    @Test
    public void getRequestParamBody() {
        String url = "/template/getRequestParamBody?id={id}" ; // 请求参数占位符

        Map<String, Object> uriVariables = new HashMap<>(); // 请求参数
        uriVariables.put("id", 1);

        Map<String, Object> request = new HashMap<>(); // 请求body
        request.put("name", "alita");
        request.put("age", 300);

        Map map = restTemplate.postForObject(url, request, Map.class, uriVariables);
        System.out.println(map);
    }

    @Test
    public void postForEntity() {
        String url = "/template/getRequestParamBody?id={id}" ; // 请求参数占位符

        Map<String, Object> uriVariables = new HashMap<>(); // 请求参数
        uriVariables.put("id", 1);

        Map<String, Object> request = new HashMap<>(); // 请求body
        request.put("name", "alita");
        request.put("age", 300);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class, uriVariables);
        System.out.println(responseEntity);
    }
}
