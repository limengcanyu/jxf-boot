package com.spring.boot.mongo.old;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Data
class Person {
    @Id
    private String id;
    private String firstname;
    private String lastname;
//    private Address address;

    // … getters and setters omitted
}

@SpringBootTest
public class QueryByExampleTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryByExampleTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOps;

    @Test
    public void simpleExample() {
        Person person = new Person(); // Create a new instance of the domain object.
        person.setFirstname("Dave"); // Set the properties to query.

        Example<Person> example = Example.of(person); // Create the Example.
        System.out.println();
    }

    @Test
    public void exampleMatchers() {
//        Person person = new Person();
//        person.setFirstname("Dave");
//
//        ExampleMatcher matcher = ExampleMatcher.matching() // Create an ExampleMatcher to expect all values to match. It is usable at this stage even without further configuration.
//                .withIgnorePaths("lastname") // Construct a new ExampleMatcher to ignore the lastname property path.
//                .withIncludeNullValues() // Construct a new ExampleMatcher to ignore the lastname property path and to include null values.
//                .withStringMatcherEnding(); // Construct a new ExampleMatcher to ignore the lastname property path, to include null values, and to perform suffix string matching.
//
//        Example<Person> example = Example.of(person, matcher); // Create a new Example based on the domain object and the configured ExampleMatcher.
    }

    @Test
    public void configuringMatcherOptions() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstname", endsWith()) // 结尾匹配
                .withMatcher("lastname", startsWith().ignoreCase()); // 开头匹配，忽略大小写


    }

    @Test
    public void ConfiguringMatcherOptionsWithLambdas() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstname", ExampleMatcher.GenericPropertyMatcher::endsWith)
                .withMatcher("firstname", ExampleMatcher.GenericPropertyMatcher::startsWith);
    }

}
