package io.github.limengcanyu;

import io.github.limengcanyu.service.MysqlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootAnnotationApplicationTests {

    @Autowired
    private MysqlService mysqlService;

    @Test
    void contextLoads() {
        System.out.println(mysqlService.addRecord());
    }

}
