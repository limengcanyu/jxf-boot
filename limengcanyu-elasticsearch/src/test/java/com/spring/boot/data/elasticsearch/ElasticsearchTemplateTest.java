package com.spring.boot.data.elasticsearch;

import com.spring.boot.elasticsearch.entity.CompanySalaryItem;
import com.spring.boot.elasticsearch.entity.SalaryComputeItem;
import com.spring.boot.elasticsearch.utils.LocalDateTimeUtils;
import com.spring.boot.elasticsearch.utils.SearchHitsUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock
 * date 2019/06/23
 */
@Slf4j
@SpringBootTest
public class ElasticsearchTemplateTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void save() {
        Person person = new Person();
//        person.setId("cb7bef");
        person.setFirstName("artanis");
        person.setLastName("xxx");
        person.setCreateTime(LocalDateTimeUtils.format(LocalDateTime.now()));

        IndexQuery indexQuery = new IndexQueryBuilder()
//                .withId(person.getId())
                .withObject(person)
                .build();
        String documentId = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("person"));
        log.debug("save Person documentId: {}", documentId);

        person = elasticsearchRestTemplate.get(documentId, Person.class);
        log.debug("query person: {}", person);


        person = new Person();
//        person.setId("cb7bef");
        person.setFirstName("samuro");
        person.setLastName("yyy");
        person.setCreateTime(LocalDateTimeUtils.format(LocalDateTime.now()));

        indexQuery = new IndexQueryBuilder()
//                .withId(person.getId())
                .withObject(person)
                .build();
        documentId = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("person"));
        log.debug("save Person documentId: {}", documentId);

        person = elasticsearchRestTemplate.get(documentId, Person.class);
        log.debug("query person: {}", person);


        person = new Person();
//        person.setId("cb7bef");
        person.setFirstName("rock");
        person.setLastName("Connor");
        person.setCreateTime(LocalDateTimeUtils.format(LocalDateTime.now()));

        indexQuery = new IndexQueryBuilder()
//                .withId(person.getId())
                .withObject(person)
                .build();
        documentId = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("person"));
        log.debug("save Person documentId: {}", documentId);

        person = elasticsearchRestTemplate.get(documentId, Person.class);
        log.debug("query person: {}", person);

    }

    @Test
    public void saveSalaryComputeItem() {
        List<CompanySalaryItem> salaryItemList = new ArrayList<>();
        salaryItemList.add(new CompanySalaryItem("item_001", "薪资项001"));
        salaryItemList.add(new CompanySalaryItem("item_002", "薪资项002"));

        SalaryComputeItem salaryComputeItem = new SalaryComputeItem(
                "tenant_000001", "company_000001", "2020-05", 1, salaryItemList
        );

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withObject(salaryComputeItem)
                .build();
        String documentId = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("salary_compute_item"));
        log.debug("save documentId: {}", documentId);

        salaryComputeItem = elasticsearchRestTemplate.get(documentId, SalaryComputeItem.class);
        log.debug("query : {}", salaryComputeItem);

    }

    @Test
    public void criteriaQuery() {
        Criteria criteria = Criteria.where("firstName").is("artanis");
        SearchHits<Person> searchHits = elasticsearchRestTemplate.search(new CriteriaQuery(criteria), Person.class, IndexCoordinates.of("person"));
        log.debug("searchHits: {}", searchHits);
        Person person = searchHits.getSearchHit(0).getContent();
        log.debug("person: {}", person);
        List<Person> personList =  SearchHitsUtils.getList(searchHits, Person.class);
        log.debug("personList: {}", personList);
    }
}
