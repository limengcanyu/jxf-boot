# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)

把Arthas安装到基础镜像里

在Dockerfile中配置以下内容:

https://hub.docker.com/_/openjdk

```Dockerfile
FROM openjdk:17.0.2-oracle
 
# copy arthas
COPY --from=./arthas:latest /opt/arthas /opt/arthas

```

在SpringBoot 2应用中加入arthas-spring-boot-starter后，spring会启动arthas，并且attach自身进程，并配合tunnel server实现远程管理。这样的方案非常适合在微服务容器环境中进行远程诊断，在容器网络环境中仅需要对外暴露tunnel server的端口。

### 部署Arthas tunnel server和WebConsole

1. 下载arthas-tunnel-server-3.6.2-fatjar.jar

2. Arthas tunnel server是一个spring boot fat jar应用，直接启动:

java -jar  arthas-tunnel-server-3.6.2-fatjar.jar

3. 启动之后，可以访问 http://127.0.01:8080 打开WebConsole

4. Arthas agent端口是7777，用于和每个SpringBoot应用的Arthas进行通信，这个端口会在后面用到

5. Spring Boot应用引入依赖:

```xml
        <dependency>
            <groupId>com.taobao.arthas</groupId>
            <artifactId>arthas-spring-boot-starter</artifactId>
            <version>3.6.1</version>
            <scope>runtime</scope>
        </dependency>

```

application.yml配置

```yml
arthas:
  agent-id: URJZ5L48RPBR2ALI5K4V  #需手工指定agent-id
  tunnel-server: ws://127.0.0.1:7777/ws

```

6. 启动Spring Boot应用

查看Endpoint信息

http://localhost:8081/actuator/arthas

7. 使用WebConsole连接

打开WebConsole 

http://127.0.0.1:8563/

分别输入Arthas agent的ip(127.0.0.1)和port(7777)，和SpringBoot应用里配置的agent-id(URJZ5L48RPBR2ALI5K4V)，点Connect即可。



