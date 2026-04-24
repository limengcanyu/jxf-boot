package com.spring.boot.flaw;

import com.spring.boot.flaw.dao.entity.ExampleTable;
import com.spring.boot.flaw.service.ExampleTableService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringBootHoleApplicationTests {

    @Resource
    private ExampleTableService exampleTableService;

    @Test
    void getListByUserId() {
        List<ExampleTable> list = exampleTableService.getListByUserId("1");
        System.out.println("1: " + list);

        // 特殊字符未转义
        list = exampleTableService.getListByUserId("%");
        System.out.println("2: " + list);

        // 特殊字符转义
        list = exampleTableService.getListByUserId("\\%");
        System.out.println("3: " + list);

    }
}
