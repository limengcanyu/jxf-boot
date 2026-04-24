# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/#build-image)

### start SkyWalking

```shell
cd E:\Skywalking\apache-skywalking-apm-8.9.1\apache-skywalking-apm-bin

cd E:\Skywalking\apache-skywalking-apm-9.3.0\apache-skywalking-apm-bin

bin\startup.bat

```

http://localhost:8080

### IDEA VM参数

```shell
-javaagent:E:/Skywalking/apache-skywalking-java-agent-8.13.0/skywalking-agent/skywalking-agent.jar
-Dskywalking.collector.backend_service=localhost:11800
-Dskywalking.agent.service_name=spring-boot-skywalking

```

### Java命令启动参数

```shell
java -javaagent:E:/Skywalking/apache-skywalking-java-agent-8.13.0/skywalking-agent/skywalking-agent.jar=agent.service_name=spring-boot-skywalking,collector.backend_service=127.0.0.1:11800 -jar xx.jar

```
