package com.spring.boot.mongo.old;

import com.spring.boot.mongo.entity.Person;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/21 0021
 */
@SpringBootTest
public class SavingUpdatingRemovingDocumentsTest {
    private static final Logger logger = LoggerFactory.getLogger(SavingUpdatingRemovingDocumentsTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOps;

    @Test
    public void savingUpdateRemove() {
        Person p = new Person("Joe", 34);

        // Insert is used to initially store the object into the database.
        mongoOps.insert(p);
        logger.info("Insert: " + p);

        // Find
        p = mongoOps.findById(p.getId(), Person.class);
        logger.info("Found: " + p);

        // Update
        mongoOps.updateFirst(query(where("name").is("Joe")), update("age", 35), Person.class);
        p = mongoOps.findOne(query(where("name").is("Joe")), Person.class);
        logger.info("Updated: " + p);

        // Delete
        mongoOps.remove(p);

        // Check that deletion worked
        List<Person> people = mongoOps.findAll(Person.class);
        logger.info("Number of people = : " + people.size());

        mongoOps.dropCollection(Person.class);
    }

    @Test
    public void updateDocumnets() {
//        WriteResult wr = mongoTemplate.updateMulti(new Query(where("accounts.accountType").is(Account.Type.SAVINGS)),
//                new Update().inc("accounts.$.balance", 50.00), Account.class);
//
//        // { $push : { "category" : { "$each" : [ "spring" , "data" ] } } }
//        new Update().push("category").each("spring", "data");
//
//        // { $push : { "key" : { "$position" : 0 , "$each" : [ "Arya" , "Arry" , "Weasel" ] } } }
//        new Update().push("key").atPosition(Update.Position.FIRST).each(Arrays.asList("Arya", "Arry", "Weasel"));
//
//        // { $push : { "key" : { "$slice" : 5 , "$each" : [ "Arya" , "Arry" , "Weasel" ] } } }
//        new Update().push("key").slice(5).each(Arrays.asList("Arya", "Arry", "Weasel"));
//
//        // { $addToSet : { "values" : { "$each" : [ "spring" , "data" , "mongodb" ] } } }
//        new Update().addToSet("values").each("spring", "data", "mongodb");
    }

    @Test
    public void UpsertingDocumnets() {
        String addr = "addr";
        mongoTemplate.upsert(
                query(where("ssn").is(1111).and("firstName").is("Joe").and("Fraizer").is("Update")),
                update("address", addr), Person.class);
    }

    @Test
    public void findingAndUpsertingDocumnets() {
        mongoTemplate.insert(new Person("Tom", 21));
        mongoTemplate.insert(new Person("Dick", 22));
        mongoTemplate.insert(new Person("Harry", 23));

        Query query = new Query(Criteria.where("name").is("Harry"));
        Update update = new Update().inc("age", 1);
        Person p = mongoTemplate.findAndModify(query, update, Person.class); // return's old person object

//        assertThat(p.getName(), is("Harry"));
//        assertThat(p.getAge(), is(23));

        p = mongoTemplate.findOne(query, Person.class);
//        assertThat(p.getAge(), is(24));
        logger.debug("age: {}", p.getAge());

        // Now return the newly updated document when updating
//        p = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Person.class);
//        assertThat(p.getAge(), is(25));

//        Query query2 = new Query(Criteria.where("name").is("Mary"));
//        p = mongoTemplate.findAndModify(query2, update, new FindAndModifyOptions().returnNew(true).upsert(true), Person.class);
//        assertThat(p.getFirstName(), is("Mary"));
//        assertThat(p.getAge(), is(1));
    }

    @Test
    public void findingAndReplacingDocumnets() {
        Optional<Person> result = mongoTemplate.update(Person.class)
                .matching(query(where("name").is("Tom"))) // The actual match query mapped against the given domain type. Provide sort, fields and collation settings via the query.
                .replaceWith(new Person("Dick"))
                .withOptions(FindAndReplaceOptions.options().upsert()) // Additional optional hook to provide options other than the defaults, like upsert.
                .as(Person.class) // An optional projection type used for mapping the operation result. If none given the initial domain type is used.
                .findAndReplace(); // Trigger the actual execution. Use findAndReplaceValue to obtain the nullable result instead of an Optional.
    }

    @Test
    public void removingDocuments() {
//        // Remove a single entity specified by its _id from the associated collection.
//        mongoTemplate.remove(tywin, "GOT");
//        // Remove all documents that match the criteria of the query from the GOT collection.
//        mongoTemplate.remove(query(where("lastname").is("lannister")), "GOT");
//        // Remove the first three documents in the GOT collection. Unlike <2>, the documents to remove are identified by their _id,
//        // executing the given query, applying sort, limit, and skip options first, and then removing all at once in a separate step.
//        mongoTemplate.remove(new Query().limit(3), "GOT");
//        // Remove all documents matching the criteria of the query from the GOT collection. Unlike <3>, documents do not get deleted in a batch but one by one.
//        mongoTemplate.findAllAndRemove(query(where("lastname").is("lannister"), "GOT");
//        // Remove the first three documents in the GOT collection. Unlike <3>, documents do not get deleted in a batch but one by one.
//        mongoTemplate.findAllAndRemove(new Query().limit(3), "GOT");
    }

    /**
     * 乐观锁
     */
    @Test
    public void optimisticLocking() {
        // Intially insert document. version is set to 0.
        Person daenerys = mongoTemplate.insert(new Person("Daenerys"));

        // Load the just inserted document. version is still 0.
        Person tmp = mongoTemplate.findOne(query(where("id").is(daenerys.getId())), Person.class);

        // Update the document with version = 0. Set the lastname and bump version to 1.
        daenerys.setName("Targaryen");
        mongoTemplate.save(daenerys);

        // Try to update the previously loaded document that still has version = 0.
        // The operation fails with an OptimisticLockingFailureException, as the current version is 1.
        mongoTemplate.save(tmp); // throws OptimisticLockingFailureException
    }
}
