package com.spring.boot.mongo.old;

import com.spring.boot.mongo.entity.Person;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.List;

@SpringBootTest
public class QueryingDocumentsTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryingDocumentsTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOps;

    @Test
    public void creatingQueryInstance() {
        mongoOps.dropCollection(Person.class);

        Person person = new Person("Joe", 34);
//        Accounts accounts = new Accounts();
//        accounts.setBalance(2000);
//        person.setAccounts(accounts);
        mongoOps.insert(person);

        person = mongoOps.findById(person.getId(), Person.class);

        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 }}");
//        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }}");
        List<Person> result = mongoTemplate.find(query, Person.class);
        System.out.println();
    }

    @Test
    public void queryDistinctValues() {
        List<String> result = mongoTemplate.query(Person.class)
                .distinct("name")
                .as(String.class) // 结果类型
                .all();
        System.out.println();
    }
}
