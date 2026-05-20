#### 项目结构：

```d
asura-akka/
├── src/main/java/org/asura/akka/
│   ├── AsuraAkkaApplication.java    # Spring Boot 启动类
│   ├── config/
│   │   └── AkkaConfig.java          # Akka 配置类
│   ├── actor/
│   │   └── GreetingActor.java       # 示例 Akka Actor
│   ├── service/
│   │   └── AkkaService.java         # Akka 服务封装
│   └── controller/
│       └── AkkaController.java      # REST API 控制器
├── src/main/resources/
│   └── application.yml              # 配置文件
└── pom.xml                          # Maven 依赖配置
```

#### 功能说明：

1. AkkaConfig - 创建并管理 Akka ActorSystem

2. GreetingActor - 示例 Actor，处理问候消息：

* 接收 GreetMessage 消息
* 返回 "Hello, {name}!"

3. AkkaService - 封装 Akka 操作：

* 使用 Patterns.ask 发送异步消息
* 处理超时和异常

4. AkkaController - 暴露 REST API：

* GET /greet?name=XXX - 返回问候消息

#### 测试方式： 启动应用后访问：

http://localhost:8080/greet?name=World

返回：

Hello, World!

#### Akka 依赖版本：

* Akka Core: 2.8.5
* Akka HTTP: 10.5.2
* 支持远程通信和集群功能
