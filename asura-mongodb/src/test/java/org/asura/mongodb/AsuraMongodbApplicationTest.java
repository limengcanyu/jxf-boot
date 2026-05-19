package org.asura.mongodb;

import com.mongodb.client.MongoCollection;
import org.asura.mongodb.entity.Inventory;
import org.asura.mongodb.entity.Product;
import org.asura.mongodb.repository.InventoryRepository;
import org.asura.mongodb.repository.ProductRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SpringBootTest
public class AsuraMongodbApplicationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    public void test1() throws InterruptedException {
        String collectionName = "product";
        mongoTemplate.dropCollection(collectionName);

        MongoCollection<Document> collection;

        boolean collectionExists = mongoTemplate.collectionExists(collectionName);
        if (!collectionExists) {
            collection = mongoTemplate.createCollection(collectionName);
        } else {
            collection = mongoTemplate.getCollection(collectionName);
        }

        Index index = new Index().unique();
        index.named("batch_productCode_index");
        index.on("batch", Sort.Direction.ASC);
        index.on("productCode", Sort.Direction.ASC);
        IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        indexOperations.ensureIndex(index);

        index = new Index().unique();
        index.named("expireDate_index");
        index.on("expireDate", Sort.Direction.ASC);
        index.expire(10, TimeUnit.SECONDS);
        indexOperations = mongoTemplate.indexOps(collectionName);
        indexOperations.ensureIndex(index);

        mongoTemplate.indexOps(collectionName).getIndexInfo().forEach(System.out::println);

        collection.listIndexes().forEach(System.out::println);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                System.out.println("Task executed by " + Thread.currentThread().getName());
                Product product = new Product();
                product.setBatch("batch1");
                product.setProductCode("productCode1");
                productRepository.save(product);

                product = new Product();
                product.setBatch("batch2");
                product.setProductCode("productCode2");
                mongoTemplate.save(product, collectionName);

                product = new Product();
                product.setBatch("batch1");
                product.setProductCode("productCode1");
                productRepository.save(product);
            });
        }

        executor.shutdown();

        boolean terminated = executor.awaitTermination(1, TimeUnit.MINUTES);
        if (terminated) {
            System.out.println("All tasks completed.");
        } else {
            System.out.println("Not all tasks completed within the given timeout.");
        }

        System.out.println("All tasks are finished after shutdown.");

        productRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void test2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("productionDate").exists(true)),
                Aggregation.match(Criteria.where("productCode").exists(true)),
                Aggregation.match(Criteria.where("storeCode").exists(true)),
                Aggregation.project("productionDate", "productCode", "storeCode")
                        .and(DateOperators.DateTrunc.truncateValueOf("productionDate").to("day")).as("productionDate")
                        .and(ConvertOperators.valueOf(DateOperators.DateToString.dateOf("productionDate").toString("%Y-%m-%d")).convertToDate()).as("productionDate")
                        .and(ConvertOperators.ToDate.toDate(DateOperators.DateToString.dateOf("productionDate").toString("%Y-%m-%d"))).as("productionDate"),
                Aggregation.group("productionDate", "productCode", "storeCode")
                                .first("productionDate").as("production_date")
                                .first("productCode").as("product_code")
                                .first("storeCode").as("store_code"),
                Aggregation.sort(Sort.Direction.DESC, "productionDate")
        );

        AtomicInteger count = new AtomicInteger();
        Stream<Inventory> inventoryStream = mongoTemplate.aggregateStream(aggregation, Inventory.class, Inventory.class);
        inventoryStream.forEach(inventory -> {
            Date curDay = inventory.getProductionDate();
            Date nextDay = DateUtils.addDays(curDay, 1);
            Criteria criteria = Criteria.where("productionDate").gte(curDay).lt(nextDay)
                    .and("productCode").is(inventory.getProductCode())
                    .and("storeCode").is(inventory.getStoreCode());
            List<Inventory> inventoryList = mongoTemplate.find(Query.query(criteria), Inventory.class);

        });

    }
}