# Akka 介绍

https://doc.akka.io/

https://guobinhit.github.io/akka-guide/

### 一、什么是 Akka

Akka 是一个用于构建高并发、分布式、容错应用的开源工具包和运行时，基于 Actor 模型实现。它由 Lightbend（原 Typesafe）公司开发，支持 Java 和 Scala 两种语言。

Akka 的核心理念是：万物皆 Actor，通过 Actor 之间的消息传递来实现并发和分布式计算。

### 二、核心概念

1. Actor 模型

Actor 是一个独立的并发实体，具有以下特点：

* 状态隔离：每个 Actor 有自己的状态，只能通过消息传递来修改
* 消息驱动：Actor 之间通过异步消息进行通信
* 位置透明：Actor 可以在本地或远程运行，调用方式相同

2. Actor System

ActorSystem 是所有 Actor 的容器和管理器：

* 负责创建和管理 Actor
* 提供调度器（Dispatcher）
* 处理消息路由和分发

3. 消息传递

* 异步：发送消息后立即返回，不阻塞
* 无保证顺序：消息不一定按发送顺序到达
* 持久化可选：可配置消息持久化保证可靠性

4. 监督策略（Supervision）

Actor 具有层级结构，父 Actor 可以监督子 Actor：

* One-For-One：只重启失败的子 Actor
* All-For-One：重启所有相关子 Actor
* Escalate：将异常向上级汇报

### 三、主要特性

特性	    说明
高并发	基于轻量级线程模型，单机可支持数百万 Actor
分布式	透明的远程 Actor 调用，支持集群部署
容错性	内置监督机制，自动恢复失败的 Actor
位置透明	本地和远程 Actor 调用方式完全一致
事件溯源	支持将状态变化记录为事件序列
流处理	Akka Streams 支持高效的数据流式处理

### 四、应用场景

1. 高吞吐量服务：API 网关、消息队列处理
2. 实时数据处理：流式计算、事件驱动系统
3. 分布式系统：微服务架构、分布式协调
4. 游戏后端：实时多人游戏服务器
5. 金融交易系统：低延迟、高可靠性要求的场景

### 五、asura-boot 中的 Akka 集成

在 asura-akka 模块中，我们提供了：

1. 配置类 (AkkaConfig)

```java
@Bean
public ActorSystem actorSystem() {
    Config config = ConfigFactory.load();
    return ActorSystem.create("asura-akka-system", config);
}
```

2. 示例 Actor (GreetingActor)

```java
public Receive createReceive() {
    return receiveBuilder()
        .match(GreetMessage.class, message -> {
            sender().tell("Hello, " + message.getName() + "!", self());
        })
        .build();
}
```

3. 服务封装 (AkkaService)

* 使用 Patterns.ask 发送异步消息
* 处理超时和异常

4. REST API (AkkaController)

```http
GET /greet?name=World → "Hello, World!"
```

### 六、核心依赖

```xml
<!-- Akka Core -->
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-actor_2.13</artifactId>
    <version>2.8.5</version>
</dependency>

<!-- Akka Remote -->
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-remote_2.13</artifactId>
    <version>2.8.5</version>
</dependency>

<!-- Akka Cluster -->
<dependency>
    <groupId>com.typesafe.akka</groupId>
    <artifactId>akka-cluster_2.13</artifactId>
    <version>2.8.5</version>
</dependency>
```

### 七、总结

Akka 通过 Actor 模型提供了一种优雅的方式来处理并发和分布式系统的复杂性。其核心优势在于：

* 简化并发编程：无需手动管理线程
* 天然分布式：透明的远程通信
* 自愈能力：内置容错和恢复机制
* 高性能：轻量级 Actor 模型支持极高并发

适合需要高可用、低延迟、高吞吐量的企业级应用场景。
