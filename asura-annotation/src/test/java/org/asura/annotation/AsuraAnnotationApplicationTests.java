package org.asura.annotation;

import org.asura.annotation.service.MysqlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsuraAnnotationApplicationTests {

    @Autowired
    private MysqlService mysqlService;

    @Test
    void contextLoads() {
        System.out.println(mysqlService.addRecord());
    }

}
