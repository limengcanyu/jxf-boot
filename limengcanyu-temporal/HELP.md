# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.5/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

1. 正常下单（30秒内完成）

curl -X POST http://localhost:8080/orders \
-H "Content-Type: application/json" \
-d '{"orderId": "ORDER123", "amount": 500}'

2. 大额订单（需风控审批）

curl -X POST http://localhost:8080/orders \
-d '{"orderId": "ORDER999", "amount": 1500}'

然后发送审批：

curl -X POST http://localhost:8080/workflow/order-workflow-ORDER999/signal/approveRisk \
-d '{"approve": true}'

3. 查询状态

curl http://localhost:8080/workflow/order-workflow-ORDER999/query/status
# 返回: WAITING_RISK_APPROVAL

4. 支付超时（30分钟后自动失败）
等待30分钟或修改 awaitWithTimeout 为 1分钟测试

5. 人工补偿确认（可选）

curl -X POST http://localhost:8080/workflow/order-workflow-ORDER123fail/signal/confirmRefund \
-d '{"confirm": true}'


测试流程
1. 启动应用（H2）
mvn spring-boot:run
访问 H2 控制台：http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:orderdb

2. 创建订单

curl -X POST http://localhost:8080/orders \
-d '{"orderId": "ORDER999", "amount": 1500}'

3. 查询数据库订单
curl http://localhost:8080/orders/ORDER999

4. 发送风控审批

curl -X POST http://localhost:8080/workflow/order-workflow-ORDER999/signal/approveRisk \
-d '{"approve": true}'
