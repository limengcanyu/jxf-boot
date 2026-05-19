package org.asura.mongodb;

import org.asura.mongodb.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionService transactionService;

    @Test
    void createCollection() {
        mongoTemplate.dropCollection("artanis");
        mongoTemplate.dropCollection("employee");

    }

    @Test
    void transactionMethod3() throws Exception {
        transactionService.transactionMethod3();
    }
}