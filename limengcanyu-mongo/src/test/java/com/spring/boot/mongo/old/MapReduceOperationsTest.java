//package com.spring.boot.mongo.old;
//
//import com.alibaba.fastjson.JSONObject;
//import com.spring.boot.mongo.entity.ValueObject;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
//import org.springframework.data.mongodb.core.query.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.data.mongodb.core.mapreduce.MapReduceOptions.options;
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//
//@SpringBootTest
//public class MapReduceOperationsTest {
//    private static final Logger logger = LoggerFactory.getLogger(MapReduceOperationsTest.class);
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private MongoOperations mongoOps;
//
//    @Test
//    public void query() {
//        mongoTemplate.dropCollection("jmr1");
//
//        Document document = new Document();
//        ObjectId id = new ObjectId("4e5ff893c0277826074ec533");
//        document.put("_id", id);
//        List<String> xList = new ArrayList<>();
//        xList.add("a");
//        xList.add("b");
//        document.put("x", xList.toArray());
//        mongoTemplate.save(document, "jmr1");
//
//        document = new Document();
//        id = new ObjectId("4e5ff893c0277826074ec534");
//        document.put("_id", id);
//        xList = new ArrayList<>();
//        xList.add("b");
//        xList.add("c");
//        document.put("x", xList.toArray());
//        mongoTemplate.save(document, "jmr1");
//
//        document = new Document();
//        id = new ObjectId("4e5ff893c0277826074ec535");
//        document.put("_id", id);
//        xList = new ArrayList<>();
//        xList.add("c");
//        xList.add("d");
//        document.put("x", xList.toArray());
//        mongoTemplate.save(document, "jmr1");
//
//        document = mongoTemplate.findById("4e5ff893c0277826074ec533", Document.class, "jmr1");
//        System.out.println(JSONObject.toJSONString(document));
//
////        MapReduceResults<ValueObject> results = mongoOps.mapReduce("jmr1", "classpath:map.js", "classpath:reduce.js", ValueObject.class);
////        for (ValueObject valueObject : results) {
////            System.out.println(valueObject);
////        }
//
//         // 静态导入
////        // static import (import static org.springframework.data.mongodb.core.mapreduce.MapReduceOptions.options;)
////        MapReduceResults<ValueObject> results = mongoOps.mapReduce("jmr1", "classpath:map.js", "classpath:reduce.js",
////                options().outputCollection("jmr1_out"), ValueObject.class);
////        for (ValueObject valueObject : results) {
////            System.out.println(valueObject);
////        }
//
//        // 指定查询条件
//        // specify a query to reduce the set of data that is fed into the Map-Reduce operation
//        Query query = new Query(where("x").ne(new String[] { "a", "b" }));
//        MapReduceResults<ValueObject> results = mongoOps.mapReduce(query, "jmr1", "classpath:map.js", "classpath:reduce.js",
//                options().outputCollection("jmr1_out"), ValueObject.class);
//        for (ValueObject valueObject : results) {
//            System.out.println(valueObject);
//        }
//    }
//}
//
