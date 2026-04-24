package com.spring.boot.mongo;

import com.spring.boot.mongo.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/05/16 09:47
 */
@Slf4j
@SpringBootTest
public class AggregationPipelineOperatorsTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void pipelineAggregationOperators() {
        Aggregation agg = newAggregation(
        );

        AggregationResults<Person> results = mongoTemplate.aggregate(agg, "person", Person.class);
        List<Person> mappedResults = results.getMappedResults();
    }

    @Test
    public void arithmeticExpressionOperators() {

    }

}
