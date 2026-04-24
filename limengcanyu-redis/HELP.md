# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/#build-image)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#data.nosql.redis)

### Guides

The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)

### IDEA VM参数

```shell
-javaagent:E:/Skywalking/apache-skywalking-java-agent-8.13.0/skywalking-agent/skywalking-agent.jar
-Dskywalking.collector.backend_service=localhost:11800
-Dskywalking.agent.service_name=spring-boot-redis

```
