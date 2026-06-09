
# Asura Boot 项目上下文（新会话快速参考）

## 项目基础信息
- **项目名称**: asura-boot
- **父POM**: Spring Boot 3.5.14
- **Java版本**: 25
- **模块数量**: 80+

## 核心依赖管理模块
- `asura-bom`: 统一管理所有依赖版本
- `asura-webmvc-deps`: Spring WebMVC 依赖聚合
- `asura-webflux-deps`: Spring WebFlux 依赖聚合
- `asura-mybatis-plus-deps`: MyBatis-Plus 依赖聚合
- `asura-common`: 公共工具类

## 模块分类概览
- **数据库**: asura-jpa, asura-mybatis-plus, asura-jooq
- **NoSQL**: asura-mongodb, asura-elasticsearch, asura-cassandra
- **消息队列**: asura-rabbitmq, asura-kafka, asura-rocketmq
- **缓存**: asura-redis, asura-redisson, asura-caffeine
- **安全**: asura-security, asura-jwt
- **监控**: asura-actuator, asura-prometheus, asura-skywalking
- **工作流**: asura-activiti, asura-flowable, asura-liteflow
- **定时任务**: asura-quartz, asura-xxl-job
- **高性能**: asura-netty, asura-disruptor
- **限流熔断**: asura-sentinel, asura-resilience4j
- **AI**: asura-ai, asura-ai-alibaba

## 关键路径
- 父POM: `asura-boot/pom.xml`
- BOM: `asura-bom/pom.xml`
- 架构文档: `ARCHITECTURE.md`

## 使用方式
所有业务模块继承父POM即可获得公共依赖：
```xml
<parent>
    <groupId>io.github.jiangxingfeng</groupId>
    <artifactId>asura-boot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```

## 快速命令
```bash
# 编译项目
mvn clean compile

# 运行特定模块
mvn spring-boot:run -pl asura-restful

# 打包
mvn clean package
```