package com.spring.boot.multi.datasource.transaction;

import com.spring.boot.multi.datasource.transaction.service.AggregateService;
import com.spring.boot.multi.datasource.transaction.service.MysqlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class SpringBootMysqlMongoTransactionApplicationTests {

    @Autowired
    private MysqlService mysqlService;

    @Autowired
    private AggregateService aggregateService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() throws Exception {
        mysqlService.saveMysqlRecord();
    }

    @Test
    void aggregate() throws Exception {
        mongoTemplate.dropCollection("employee");
        mongoTemplate.createCollection("employee");

        aggregateService.aggregate();
    }

    @Test
    void saveItemData() throws Exception {
        mongoTemplate.dropCollection("item_record");
        mongoTemplate.createCollection("item_record");

        aggregateService.saveItemData();
    }

}
