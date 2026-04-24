//package com.spring.boot.mongo.old;
//
//import com.spring.boot.mongo.entity.Venue;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.geo.Box;
//import org.springframework.data.geo.Circle;
//import org.springframework.data.geo.Point;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//
//import java.util.List;
//
//@SpringBootTest
//public class GeoSpatialQueriesTest {
//    private static final Logger logger = LoggerFactory.getLogger(GeoSpatialQueriesTest.class);
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private MongoOperations mongoOps;
//
//    @Test
//    public void query() {
//        // To find locations within a Circle, you can use the following query:
//        Circle circle = new Circle(-73.99171, 40.738868, 0.01);
//        List<Venue> venues = mongoTemplate.find(new Query(Criteria.where("location").within(circle)), Venue.class);
//
//        // To find venues within a Circle using spherical coordinates, you can use the following query:
//        circle = new Circle(-73.99171, 40.738868, 0.003712240453784);
//        venues = mongoTemplate.find(new Query(Criteria.where("location").withinSphere(circle)), Venue.class);
//
//        // To find venues within a Box, you can use the following query:
//        // lower-left then upper-right
//        Box box = new Box(new Point(-73.99756, 40.73083), new Point(-73.988135, 40.741404));
//        venues = mongoTemplate.find(new Query(Criteria.where("location").within(box)), Venue.class);
//
//        // To find venues near a Point, you can use the following queries:
//        Point point = new Point(-73.99171, 40.738868);
//        venues = mongoTemplate.find(new Query(Criteria.where("location").near(point).maxDistance(0.01)), Venue.class);
//        point = new Point(-73.99171, 40.738868);
//        venues = mongoTemplate.find(new Query(Criteria.where("location").near(point).minDistance(0.01).maxDistance(100)), Venue.class);
//
//        // To find venues near a Point using spherical coordinates, you can use the following query:
//        point = new Point(-73.99171, 40.738868);
//        venues = mongoTemplate.find(new Query(Criteria.where("location").nearSphere(point).maxDistance(0.003712240453784)), Venue.class);
//    }
//}
