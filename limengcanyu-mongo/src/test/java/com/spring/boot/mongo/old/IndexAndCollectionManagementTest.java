package com.spring.boot.mongo.old;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootTest
public class IndexAndCollectionManagementTest {
    private static final Logger logger = LoggerFactory.getLogger(IndexAndCollectionManagementTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoOperations mongoOps;

    @Test
    public void creatingIndex() {
        mongoTemplate.indexOps(Person.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));
    }

    @Test
    public void indexTest() {
//        // 新增测试数据
//        mongoTemplate.dropCollection("index_test");
//
//        List<Document> documentList = new ArrayList<>();
//        int count = 0;
//        for (int tenantIndex = 1; tenantIndex <= 50000; tenantIndex++) {
//            for (int batchIndex = 1; batchIndex <= 3; batchIndex++) {
//                for (int employeeIndex = 1; employeeIndex <= 100; employeeIndex++) {
//                    for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
//                        Document document = new Document();
//                        document.put("tenant_id", "tenant_00000" + tenantIndex);
//                        document.put("company_id", "company_000001");
//
//                        if (monthIndex <= 9) {
//                            document.put("salary_month", "2019-0" + monthIndex);
//                        } else {
//                            document.put("salary_month", "2019-" + monthIndex);
//                        }
//
//                        document.put("salary_batch", batchIndex);
//                        document.put("employee_id", "employee_00000" + employeeIndex);
//
//                        for (int j = 1; j <= 100; j++) {
//                            document.put("item_00" + j, 1.0 + j);
//                        }
//
//                        documentList.add(document);
//
//                        if (documentList.size() / 50000 == 0) {
//                            mongoTemplate.insert(documentList, "index_test");
//                            documentList.clear();
//                        }
//
//                        count++;
//
//                        logger.debug("当前记录数量: {}", count);
//                    }
//                }
//            }
//        }
//
//        logger.debug("新增记录数量: {}", count);

//        // 查询记录数
//        Query query = Query.query(
//                Criteria.where("tenant_id").gte("tenant_000001")
//        );
//        long count = mongoTemplate.count(query, "index_test");
//        logger.debug("count: {}", count);

//        // 创建索引
//        mongoTemplate.indexOps("index_test").dropAllIndexes();
//        long start = System.currentTimeMillis();
//        mongoTemplate.indexOps("index_test").ensureIndex(
//                new Index().named("index_test_collection_index").on("tenant_id", Sort.Direction.ASC).on("company_id", Sort.Direction.ASC)
//                        .on("salary_month", Sort.Direction.ASC).on("salary_batch", Sort.Direction.ASC)
//        );
//        logger.debug("创建索引耗时毫秒: {}", System.currentTimeMillis() - start);

//        // 查看索引
//        List<IndexInfo> indexInfoList = mongoTemplate.indexOps("index_test").getIndexInfo();
//        logger.debug("indexInfoList: {}", indexInfoList);
//        if (!CollectionUtils.isEmpty(indexInfoList)) {
//            indexInfoList.forEach(indexInfo -> {
//                logger.debug("index name: {}", indexInfo.getName());
//            });
//        }

        // 查看查询时间
        long start = System.currentTimeMillis();
        Query query = Query.query(
                Criteria.where("tenant_id").is("tenant_0000030000").and("company_id").is("company_000001")
                        .and("salary_month").is("2019-11")
//                        .and("salary_batch").is(1)
//                        .and("employee_id").is("employee_000001")
        );
        List<Document> resultDocumentList = mongoTemplate.find(query.skip(1000000).limit(20), Document.class, "index_test");
        logger.debug("执行查询耗时毫秒: {}", System.currentTimeMillis() - start);
    }

}
