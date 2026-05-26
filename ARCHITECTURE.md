
# Asura Boot 项目架构文档

## 1. 项目概述

Asura Boot 是一个基于 Spring Boot 的企业级微服务架构模板，包含 80+ 个子模块，涵盖数据库、消息队列、缓存、监控、安全等多个技术领域。

## 2. 模块分类

### 2.1 依赖管理模块

| 模块名称 | 作用 | 打包方式 |
| :--- | :--- | :--- |
| `asura-bom` | 统一管理所有依赖版本 | pom |
| `asura-webmvc-deps` | 聚合 Spring WebMVC 相关依赖 | pom |
| `asura-webflux-deps` | 聚合 Spring WebFlux 相关依赖 | pom |
| `asura-mybatis-plus-deps` | 聚合 MyBatis-Plus 全家桶依赖 | pom |

### 2.2 公共工具模块

| 模块名称 | 作用 | 说明 |
| :--- | :--- | :--- |
| `asura-common` | 存放通用工具类和实体 | 包含 HttpUtils、User 等 |

### 2.3 数据库 & ORM

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-jpa` | Spring Data JPA | ORM框架 |
| `asura-mybatis-plus` | MyBatis-Plus | ORM框架 |
| `asura-jooq` | jOOQ | SQL构建器 |
| `asura-jdbc` | Spring JDBC | 原生JDBC |

### 2.4 NoSQL 数据库

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-mongodb` | MongoDB | 文档数据库 |
| `asura-elasticsearch` | Elasticsearch | 搜索引擎 |
| `asura-cassandra` | Cassandra | 分布式数据库 |
| `asura-couchbase` | Couchbase | 内存数据库 |
| `asura-neo4j` | Neo4j | 图数据库 |

### 2.5 消息队列

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-rabbitmq` | RabbitMQ | 消息队列 |
| `asura-rabbitmq-producer` | RabbitMQ | 生产者 |
| `asura-rabbitmq-consumer` | RabbitMQ | 消费者 |
| `asura-kafka` | Kafka | 分布式消息队列 |
| `asura-kafka-streams` | Kafka Streams | 流处理 |
| `asura-rocketmq` | RocketMQ | 阿里消息队列 |
| `asura-pulsar` | Pulsar | 云原生消息队列 |

### 2.6 缓存 & 分布式

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-redis` | Spring Data Redis | Redis客户端 |
| `asura-redisson` | Redisson | Redis客户端 |
| `asura-redis-jedis` | Jedis | Redis客户端 |
| `asura-redis-reactive` | Reactive Redis | 响应式Redis |
| `asura-caffeine` | Caffeine | 本地缓存 |
| `asura-lock4j-redisson` | Lock4j + Redisson | 分布式锁 |
| `asura-lock4j-zookeeper` | Lock4j + ZooKeeper | 分布式锁 |

### 2.7 安全

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-security` | Spring Security | 安全框架 |
| `asura-jwt` | JJWT | JWT认证 |

### 2.8 API文档

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-knife4j` | Knife4j | API文档工具 |

### 2.9 监控 & 追踪

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-actuator` | Spring Actuator | 监控指标 |
| `asura-prometheus` | Prometheus | 监控系统 |
| `asura-skywalking` | SkyWalking | 链路追踪 |
| `asura-arthas` | Arthas | 诊断工具 |

### 2.10 工作流

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-activiti` | Activiti | 工作流引擎 |
| `asura-flowable` | Flowable | 工作流引擎 |
| `asura-camunda` | Camunda | 工作流引擎 |
| `asura-liteflow` | LiteFlow | 规则引擎 |

### 2.11 定时任务

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-quartz` | Quartz | 定时任务框架 |
| `asura-xxl-job` | XXL-Job | 分布式任务调度 |

### 2.12 高性能

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-netty` | Netty | 网络框架 |
| `asura-netty-server` | Netty | 服务端 |
| `asura-netty-client` | Netty | 客户端 |
| `asura-disruptor` | Disruptor | 高性能队列 |

### 2.13 限流熔断

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-sentinel` | Sentinel | 限流熔断 |
| `asura-resilience4j` | Resilience4j | 容错框架 |
| `asura-rate-limit` | 自定义 | 限流模块 |

### 2.14 AI相关

| 模块名称 | 技术栈 | 说明 |
| :--- | :--- | :--- |
| `asura-ai` | Spring AI | AI集成 |
| `asura-ai-alibaba` | Spring AI Alibaba | 阿里AI集成 |

## 3. 依赖管理架构

```
asura-boot (父POM)
    ├── asura-bom (BOM依赖版本管理)
    │       └── 所有依赖版本定义
    │
    ├── asura-webmvc-deps (WebMVC依赖聚合)
    ├── asura-webflux-deps (WebFlux依赖聚合)
    ├── asura-mybatis-plus-deps (MyBatis-Plus依赖聚合)
    │
    └── 业务模块 (asura-restful, asura-redis, ...)
            └── 通过 <parent> 继承父POM
                └── 自动获得所有公共依赖和版本管理
```

## 4. 模块命名规范

| 后缀 | 含义 | 示例 |
| :--- | :--- | :--- |
| `-deps` | 依赖管理模块 | `asura-webmvc-deps` |
| `-common` | 公共工具模块 | `asura-common` |
| `-server` | 服务端模块 | `asura-netty-server` |
| `-client` | 客户端模块 | `asura-admin-client` |
| `-producer` | 生产者模块 | `asura-rabbitmq-producer` |
| `-consumer` | 消费者模块 | `asura-rabbitmq-consumer` |

## 5. 核心技术栈版本

| 技术 | 版本 | 说明 |
| :--- | :--- | :--- |
| Spring Boot | 3.5.14 | 基础框架 |
| Spring Cloud | 2025.0.2 | 微服务框架 |
| Spring AI | 1.1.6 | AI集成 |
| MyBatis-Plus | 3.5.16 | ORM框架 |
| Redis | 4.3.1 (Redisson) | 缓存 |
| Netty | 4.2.12.Final | 网络框架 |
| Java | 25 | 编程语言 |

## 6. 使用指南

### 6.1 继承父POM

```xml
<parent>
    <groupId>io.github.jiangxingfeng</groupId>
    <artifactId>asura-boot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```

### 6.2 引入依赖管理模块

```xml
<dependency>
    <groupId>org.asura.webmvc.deps</groupId>
    <artifactId>asura-webmvc-deps</artifactId>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

## 7. 扩展建议

### 7.1 添加新模块步骤

1. 在父POM的 `<modules>` 中添加新模块
2. 创建模块目录和 `pom.xml`
3. 继承父POM
4. 添加必要的依赖
5. 编写业务代码

### 7.2 版本管理

- 所有依赖版本统一在 `asura-bom` 中管理
- 使用 `${project.version}` 保持版本一致
- 定期更新依赖版本并进行兼容性测试