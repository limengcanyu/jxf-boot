package com.spring.boot.mongo;

import com.spring.boot.mongo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * <p>Description: </p>
 *
 * @author rock.jxf
 * @date 2020/12/4 21:50
 */
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

//        mongoTemplate.createCollection("artanis");
//        mongoTemplate.createCollection("employee");

    }

    @Test
    void transactionMethod3() throws Exception {
        transactionService.transactionMethod3();
    }
}
