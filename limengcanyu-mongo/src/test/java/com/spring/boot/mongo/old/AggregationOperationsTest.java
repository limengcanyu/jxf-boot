package com.spring.boot.mongo.old;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@SpringBootTest
public class AggregationOperationsTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void createData() {
        List<Document> documentList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Document document = new Document();
            document.put("tenant_id", "tenant_000001");
            document.put("company_id", "company_000001");
            document.put("salary_month", "2019-12");
            document.put("salary_batch", 1);

            document.put("employee_id", "employee_00000" + i);

            document.put("item_020", 1.00);

            documentList.add(document);
        }

        mongoTemplate.insert(documentList, "group_test");

    }

    @Test
    public void createCollection() {
        mongoTemplate.createCollection("group_test");
    }

    @Test
    public void dropCollection() {
        mongoTemplate.dropCollection("group_test");
    }

    @Test
    public void groupSum() {
        Aggregation aggregation = newAggregation(
                group("tenant_id", "company_id", "salary_month").sum("item_001").as("item001"),
                sort(ASC, "tenant_id", "company_id", "salary_month", "item001")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "group_test", Document.class);
        List<Document> mappedResults = result.getMappedResults();
    }

    @Test
    public void groupCount() {
        Set<String> employeeIdSet = new HashSet<>();
        employeeIdSet.add("employee_000001");
        employeeIdSet.add("employee_000002");
        employeeIdSet.add("employee_000003");
        employeeIdSet.add("employee_000004");
        employeeIdSet.add("employee_000005");

        Criteria criteria = Criteria.where("tenant_id").is("tenant_000001").and("company_id").is("company_000001")
                .and("salary_month").gte("2019-05").lt("2020-04")
                .and("employee_id").in(employeeIdSet)
                .and("item_020").gt(0.00);

        List<Document> employeeList = mongoTemplate.find(Query.query(criteria), Document.class, "group_test");

        Aggregation aggregation = newAggregation(
                match(criteria),
                group("company_id").count().as("count")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "group_test", Document.class);
        List<Document> mappedResults = result.getMappedResults();
    }

    @Data
    public class TagCount {
        String tag;
        int n;
    }

    @Test
    public void projectFields() {
        Aggregation agg = newAggregation(
                // 选中操作字段
                project("company_id", "employee_id")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "group_test", Document.class);
        List<Document> getMappedResults = results.getMappedResults();
        log.debug("getMappedResults: {}", getMappedResults);
    }

    /**
     * 展开数组字段，根据每个值生成一个新文档
     * <p>
     * Unwind Array
     * <p>
     * From the mongo shell, create a sample collection named inventory with the following document:
     * <p>
     * db.inventory.insertOne({ "_id" : 1, "item" : "ABC1", sizes: [ "S", "M", "L"] })
     * <p>
     * The following aggregation uses the $unwind stage to output a document for each element in the sizes array:
     * <p>
     * db.inventory.aggregate( [ { $unwind : "$sizes" } ] )
     * <p>
     * The operation returns the following results:
     * <p>
     * { "_id" : 1, "item" : "ABC1", "sizes" : "S" }
     * { "_id" : 1, "item" : "ABC1", "sizes" : "M" }
     * { "_id" : 1, "item" : "ABC1", "sizes" : "L" }
     */
    @Test
    public void unwindArray() {
        Aggregation agg = newAggregation(
                unwind("sizes")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "inventory", Document.class);
        List<Document> getMappedResults = results.getMappedResults();
        log.debug("getMappedResults: {}", getMappedResults);
    }

    @Data
    public class ZipInfo {
        String id;
        String city;
        String state;
        @Field("pop")
        int population;
        @Field("loc")
        double[] location;
    }

    @Data
    public class City {
        String name;
        int population;
    }

    @Data
    public class ZipInfoStats {
        String id;
        String state;
        City biggestCity;
        City smallestCity;
    }

    @Test
    public void example2() {
        ZipInfo zipInfo = new ZipInfo();
        zipInfo.setId("1");
        zipInfo.setCity("上海");
        zipInfo.setState("中国");
        zipInfo.setPopulation(1000);
        zipInfo.setLocation(new double[]{100.00, 200.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        zipInfo = new ZipInfo();
        zipInfo.setId("2");
        zipInfo.setCity("北京");
        zipInfo.setState("中国");
        zipInfo.setPopulation(2000); // 人口
        zipInfo.setLocation(new double[]{101.00, 201.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        zipInfo = new ZipInfo();
        zipInfo.setId("3");
        zipInfo.setCity("北京");
        zipInfo.setState("中国");
        zipInfo.setPopulation(1000); // 人口
        zipInfo.setLocation(new double[]{103.00, 203.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        zipInfo = new ZipInfo();
        zipInfo.setId("4");
        zipInfo.setCity("南京");
        zipInfo.setState("中国");
        zipInfo.setPopulation(1000); // 人口
        zipInfo.setLocation(new double[]{103.00, 203.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        zipInfo = new ZipInfo();
        zipInfo.setId("5");
        zipInfo.setCity("加州");
        zipInfo.setState("美国");
        zipInfo.setPopulation(1200); // 人口
        zipInfo.setLocation(new double[]{123.00, 223.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        zipInfo = new ZipInfo();
        zipInfo.setId("6");
        zipInfo.setCity("洛杉矶");
        zipInfo.setState("美国");
        zipInfo.setPopulation(1300); // 人口
        zipInfo.setLocation(new double[]{133.00, 233.00});
        zipInfo = mongoTemplate.save(zipInfo);
        log.debug("save: {}", zipInfo);

        // (id=1, city=上海, state=中国, population=1000, location=[100.0, 200.0])
        // (id=2, city=北京, state=中国, population=2000, location=[101.0, 201.0])
        // (id=3, city=北京, state=中国, population=1000, location=[103.0, 203.0])
        // (id=4, city=南京, state=中国, population=1000, location=[103.0, 203.0])
        // (id=5, city=加州, state=美国, population=1200, location=[123.0, 223.0])
        // (id=6, city=洛杉矶, state=美国, population=1300, location=[133.0, 233.0])

        TypedAggregation<ZipInfo> aggregation = newAggregation(ZipInfo.class,
                // 根据"state", "city"分组，对"population"求和，结果别名为"pop"
                group("state", "city")
                        .sum("population").as("pop"),
                // 对"pop", "state", "city"按升序排序
                sort(ASC, "pop", "state", "city"),

                // 至此处结果: [
                // {"state":"中国","city":"上海","pop":1000},
                // {"state":"中国","city":"南京","pop":1000},
                // {"state":"美国","city":"加州","pop":1200},
                // {"state":"美国","city":"洛杉矶","pop":1300},
                // {"state":"中国","city":"北京","pop":3000}
                // ]

                // 根据"state"分组，
                group("state")
                        .last("city").as("biggestCity") // 获取最后一个"city"，别名"biggestCity"
                        .last("pop").as("biggestPop") // 获取最后一个"pop"，别名"biggestPop"
                        .first("city").as("smallestCity") // 获取第一个"city"，别名"smallestCity"
                        .first("pop").as("smallestPop"), // 获取第一个"pop"，别名"smallestPop"

                // [
                // {"_id":"美国","biggestCity":"洛杉矶","biggestPop":1300,"smallestCity":"加州","smallestPop":1200},
                // {"_id":"中国","biggestCity":"北京","biggestPop":3000,"smallestCity":"上海","smallestPop":1000}
                // ]

                project().and("state")
                        // 将先前操作的结果投射到当前字段，即project().and("state")中选中的字段
                        .previousOperation()
                        // 根据指定名字定义字段
                        .and("biggestCity")
                        // 在当前字段定义嵌套字段
                        .nested(
                                // 从指定名称和目标创建字段
                                bind("name", "biggestCity").and("population", "biggestPop")
                        )
                        // 根据指定名字定义字段
                        .and("smallestCity")
                        // 在当前字段定义嵌套字段
                        .nested(
                                // 从指定名称和目标创建字段
                                bind("name", "smallestCity").and("population", "smallestPop")
                        ),

                // 至此处结果: [
                // {"biggestCity":{"name":"洛杉矶","population":1300},"smallestCity":{"name":"加州","population":1200},"state":"美国"},
                // {"biggestCity":{"name":"北京","population":3000},"smallestCity":{"name":"上海","population":1000},"state":"中国"}
                // ]

                // 对"state"按升序排序
                sort(ASC, "state")

                // 至此处结果: [
                // {"biggestCity":{"name":"北京","population":3000},"smallestCity":{"name":"上海","population":1000},"state":"中国"},
                // {"biggestCity":{"name":"洛杉矶","population":1300},"smallestCity":{"name":"加州","population":1200},"state":"美国"}
                // ]
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, Document.class);
        List<Document> zipInfoStatsList = result.getMappedResults();

//        AggregationResults<ZipInfoStats> result = mongoTemplate.aggregate(aggregation, ZipInfoStats.class);
//        List<ZipInfoStats> zipInfoStatsList = result.getMappedResults();
//        if (!CollectionUtils.isEmpty(zipInfoStatsList)) {
//            ZipInfoStats firstZipInfoStats = zipInfoStatsList.get(0);
//        }
//
//        System.out.println(zipInfoStatsList);

        mongoTemplate.dropCollection(ZipInfo.class);
    }

    class StateStats {
        @Id
        String id;
        String state;
        @Field("totalPop")
        int totalPopulation;
    }

    @Test
    public void example3() {
        TypedAggregation<ZipInfo> agg = newAggregation(ZipInfo.class,
                // 1. Group the input collection by the state field and calculate the sum of the population field and store the result in the new field "totalPop".
                // 分组之后求和
                group("state").sum("population").as("totalPop"),
                // 2. Sort the intermediate result by the id-reference of the previous group operation in addition to the "totalPop" field in ascending order.
                sort(ASC, previousOperation(), "totalPop"),
                // 3. Filter the intermediate result by using a match operation which accepts a Criteria query as an argument.
                match(where("totalPop").gte(10 * 1000 * 1000))
        );

        AggregationResults<StateStats> result = mongoTemplate.aggregate(agg, StateStats.class);
        List<StateStats> stateStatsList = result.getMappedResults();
        System.out.println();
    }

    class Product {
        String id;
        String name;
        double netPrice;
        int spaceUnits;
        double discountRate;
        double taxRate;
    }

    @Test
    public void example4() {
        TypedAggregation<Product> agg = newAggregation(Product.class,
                project("name", "netPrice")
                        .and("netPrice").plus(1).as("netPricePlus1")
                        .and("netPrice").minus(1).as("netPriceMinus1")
                        .and("netPrice").multiply(1.19).as("grossPrice")
                        .and("netPrice").divide(2).as("netPriceDiv2")
                        .and("spaceUnits").mod(2).as("spaceUnitsMod2")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(agg, Document.class);
        List<Document> resultList = result.getMappedResults();
        System.out.println();
    }

    @Test
    public void example5() {
        TypedAggregation<Product> agg = newAggregation(Product.class,
                project("name", "netPrice")
                        .andExpression("netPrice + 1").as("netPricePlus1")
                        .andExpression("netPrice - 1").as("netPriceMinus1")
                        .andExpression("netPrice / 2").as("netPriceDiv2")
                        .andExpression("netPrice * 1.19").as("grossPrice")
                        .andExpression("netPrice % 2").as("spaceUnitsMod2")
                        .andExpression("(netPrice * 0.8  + 1.2) * 1.19").as("grossPriceIncludingDiscountAndCharge")

        );

        AggregationResults<Product> result = mongoTemplate.aggregate(agg, Product.class);
        List<Product> mappedResults = result.getMappedResults();
    }

    @Test
    public void example6() {
        double shippingCosts = 1.2;

        TypedAggregation<Product> agg = newAggregation(Product.class,
                project("name", "netPrice")
                        .andExpression("(netPrice * (1-discountRate)  + [0]) * (1+taxRate)", shippingCosts).as("salesPrice")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(agg, Document.class);
        List<Document> mappedResults = result.getMappedResults();
    }

    public class InventoryItem {

        @Id
        int id;
        String item;
        String description;
        int qty;
    }

    public class InventoryItemProjection {

        @Id
        int id;
        String item;
        String description;
        int qty;
        int discount;
    }

    @Test
    public void example7() {
//        TypedAggregation<InventoryItem> agg = newAggregation(InventoryItem.class,
//                project("item").and("discount")
//                        .applyCondition(ConditionalOperator.newBuilder().when(Criteria.where("qty").gte(250))
//                                .then(30)
//                                .otherwise(20))
//                        .and(ifNull("description", "Unspecified")).as("description")
//        );
//
//        AggregationResults<InventoryItemProjection> result = mongoTemplate.aggregate(agg, "inventory", InventoryItemProjection.class);
//        List<InventoryItemProjection> stateStatsList = result.getMappedResults();

        // =============================================================================================================
//        // Conditional aggregation projection
//        TypedAggregation<Book> agg = Aggregation.newAggregation(Book.class,
//                project("title")
//                        .and(ConditionalOperators.when(ComparisonOperators.valueOf("author.middle")
//                                .equalToValue(""))
//                                .then("$$REMOVE")
//                                .otherwiseValueOf("author.middle")
//                        )
//                        .as("author.middle"));
    }
}
