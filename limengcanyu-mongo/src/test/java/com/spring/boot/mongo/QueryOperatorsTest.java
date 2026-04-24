package com.spring.boot.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.spring.boot.mongo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 09:08
 */
@Slf4j
@SpringBootTest
public class QueryOperatorsTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void saveEntity() {
        Person person = new Person("rock", 21, "20 + 1 - 15");
        log.debug("save before person: {}", person);
        person = mongoTemplate.save(person);
        log.debug("save after person: {}", person);

        person = new Person("jessica", 20, "30 + 1 - 15");
        log.debug("save before person: {}", person);
        person = mongoTemplate.save(person);
        log.debug("save after person: {}", person);
    }

    @Test
    public void queryEntity() {
        List<Person> personList = mongoTemplate.query(Person.class)
                .matching(Query.query(Criteria.where("age").lt(50).and("accounts.balance").gt(1000.00d)).skip(50).limit(50))
                .all();

        personList = mongoTemplate.findAll(Person.class);
    }

    @Test
    public void plainJSONQuery() {
        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, name : { $gt : '1000.00' }}");
        List<Person> personList = mongoTemplate.find(query, Person.class);
    }

    @Test
    public void updateEntity() {
        Criteria criteria = Criteria.where("name").is("rock");

        Document updateDocument = new Document();
        updateDocument.put("age", 31);
        updateDocument.put("expression", "20 + 1 - 10");

        Update update = Update.fromDocument(updateDocument, "");

        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, Person.class);
        log.debug("updateResult: {}", updateResult);
        log.debug("updateResult getMatchedCount: {}", updateResult.getMatchedCount());
        log.debug("updateResult getModifiedCount: {}", updateResult.getModifiedCount());
        log.debug("updateResult wasAcknowledged: {}", updateResult.wasAcknowledged());
    }

    @Test
    public void deleteEntity() {
        Criteria criteria = Criteria.where("name").is("samuro");

        DeleteResult deleteResult = mongoTemplate.remove(Query.query(criteria), Person.class);
        log.debug("deleteResult: {}", deleteResult);
        log.debug("deleteResult getDeletedCount: {}", deleteResult.getDeletedCount());
        log.debug("deleteResult wasAcknowledged: {}", deleteResult.wasAcknowledged());
    }


}
