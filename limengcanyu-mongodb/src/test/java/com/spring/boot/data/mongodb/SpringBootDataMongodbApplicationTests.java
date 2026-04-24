package com.spring.boot.data.mongodb;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.spring.boot.data.mongodb.entity.LabelInventory;
import com.spring.boot.data.mongodb.repository.LabelInventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
class SpringBootDataMongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LabelInventoryRepository labelInventoryRepository;

    @Test
    void test1() {
        String collectionName = "label_inventory";

        mongoTemplate.dropCollection(collectionName);

        MongoCollection<Document> collection;

        boolean collectionExists = mongoTemplate.collectionExists(collectionName);
        if (collectionExists) {
            collection = mongoTemplate.getCollection(collectionName);
        } else {
            collection = mongoTemplate.createCollection(collectionName);
        }


        // 创建多列唯一索引
        Index index = new Index().unique();
        index.on(LabelInventory.Fields.batch, Sort.Direction.ASC);
        index.on(LabelInventory.Fields.productCode, Sort.Direction.ASC);
        index.named("Batch_ProductCode_Index");
        IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        indexOperations.ensureIndex(index);

        // 创建TTL索引
        index = new Index();
        index.on("expireTime", Sort.Direction.ASC);
        index.expire(60, TimeUnit.SECONDS); // 新增文档在60秒后自动删除
        index.named("ExpireTime_TTL_Index");
        indexOperations.ensureIndex(index);

        // 查询索引
        List<IndexInfo> indexInfoList = indexOperations.getIndexInfo();
        ListIndexesIterable<Document> indexesIterable = collection.listIndexes();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
//                collection.insertOne()
//                mongoTemplate.insert()
            });
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.error("线程池没有在规定时间内关闭，可能存在未完成任务");
            } else {
                log.info("所有任务已完成，线程池已关闭");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("线程被打断，可能需要进一步处理");
            throw new RuntimeException(e);
        }

    }

    @Test
    void test2() throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date day1 = DateUtils.parseDate("2024-01-01 01:00:00", pattern);
        Date day2 = DateUtils.parseDate("2024-01-01 02:00:00", pattern);

        day1 = DateUtils.truncate(day1, Calendar.DAY_OF_MONTH);
        log.info("day1: {}", sdf.format(day1));

        DateUtils.truncatedCompareTo(day1, day2, Calendar.DAY_OF_MONTH);
        DateUtils.truncatedEquals(day1, day2, Calendar.DAY_OF_MONTH);

        Query query = new Query();
        Date curDay = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date nextDay = DateUtils.addDays(curDay, 1);
        Criteria.where("updateTime").gte(curDay).lt(nextDay)
                .and("storageConditions").in("冷冻", "冷藏")
                .and("quantity").lt(BigDecimal.ZERO); // 数量<0
        query.with(Sort.by(Sort.Order.asc("productionDate")));

        log.info("query: {}", query);
        log.info("queryObject: {}", query.getQueryObject());
    }

    @Test
    void test3() {
        LabelInventory labelInventory = new LabelInventory();
        labelInventory.setStorageConditions("冷冻");
        labelInventory.setUpdateTime(new Date());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("updateTime", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("storageConditions", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("id"); // 忽略id字段
        log.info("matcher: {}", matcher.getPropertySpecifiers().getSpecifiers());

        Example<LabelInventory> example = Example.of(labelInventory);
        log.info("example: {}", example);

        labelInventoryRepository.findAll(example);
    }

    @Test
    void test4() {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(Aggregation.project("storageConditions").and(ConditionalOperators.switchCases(
                ConditionalOperators.Switch.CaseOperator.when(
                        BooleanOperators.And.and(
                                ComparisonOperators.Eq.valueOf("storageConditions").equalTo("冷藏")
                        )
                ).then(AccumulatorOperators.Sum.sumOf("quantity")),
                ConditionalOperators.Switch.CaseOperator.when(
                        BooleanOperators.And.and(
                                ComparisonOperators.Eq.valueOf("storageConditions").equalTo("冷冻")
                        )
                ).then(AccumulatorOperators.Sum.sumOf("quantity")),
                ConditionalOperators.Switch.CaseOperator.when(
                        BooleanOperators.And.and(
                                ComparisonOperators.Eq.valueOf("storageConditions").equalTo("常温")
                        )
                ).then(AccumulatorOperators.Sum.sumOf("quantity"))
        ).defaultTo("0.0")).as("totalQuantity"));

        Aggregation aggregation = Aggregation.newAggregation(operationList);
        List<LabelInventory> labelInventoryList = mongoTemplate.aggregate(aggregation, "label_inventory", LabelInventory.class).getMappedResults();
    }

    @Test
    void test5() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("productCode").is("1111")),

                Aggregation.project("productionDate", "productCode", "storeCode", "batch", "batchSet")
                        .and(DateOperators.DateTrunc.truncateValue("productionDate").to("day")).as("productionDate")
//                        .and(DateOperators.DateTrunc.truncateValue("productionDate").to("month")).as("productionDate")
//                        .and(ConvertOperators.valueOf(DateOperators.DateToString.dateOf("productionDate").toString("%Y-%m-%d")).convertToDate()).as("productionDate")
//                        .and(ConvertOperators.ToDate.toDate(DateOperators.DateToString.dateOf("productionDate").toString("%Y-%m-%d"))).as("productionDate")
                        .and(StringOperators.Concat.valueOf("batch").concat(" = ").concatValueOf("storeCode")).as("batchStoreCode"), // 只能拼接不同字段，不能拼接同一个字段的所有值

                Aggregation.group("productionDate", "productCode", "storeCode")
                        .first("productCode").as("product_code")
                        .first("storeCode").as("store_code")
                        .addToSet("batch").as("batchSet") // 输出类型只能用Map
                        .sum("quantity").as("quantity"),

                Aggregation.sort(Sort.by(Sort.Direction.DESC, "productionDate"))
        );

        Stream<Map> mapStream = mongoTemplate.aggregateStream(aggregation, LabelInventory.class, Map.class);
        mapStream.forEach(labelInventory -> {
            Object batchSet = labelInventory.get("batchSet");
            if (batchSet instanceof List<?> list) {
                log.info("batchSet: {}", String.join(",", list.stream().map(Object::toString).toList()));
            }
        });
    }

}
