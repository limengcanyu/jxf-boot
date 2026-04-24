package com.spring.boot.mongo.old;

import com.spring.boot.mongo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/04/11 12:04
 */
@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() {
        // 在 mongo replicaSet 中，事务操作中不支持 createCollection，dropCollection，createIndex，dropIndex
        mongoTemplate.dropCollection("artanis");
        mongoTemplate.createCollection("artanis");

        try {
            transactionService.transactionMethod1();
//            transactionService.transactionMethod2();
        } catch (Exception e) {
            e.printStackTrace();
        }

        transactionService.select();
    }
}
