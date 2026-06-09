### 项目结构

```d
asura-ddd-structure/
├── common/                              # 公共组件
│   └── dto/response/ApiResponse.java    # 统一API响应封装
├── infrastructure/                      # 全局基础设施
│   └── exception/GlobalExceptionHandler.java
├── user/                                # 用户领域
│   ├── application/                     # 应用层
│   │   ├── dto/command/                 # 命令DTO
│   │   ├── dto/response/                # 响应DTO
│   │   └── service/UserApplicationService.java
│   ├── domain/                          # 领域层
│   │   ├── model/aggregate/User.java
│   │   ├── model/valueobject/           # Address, PhoneNumber
│   │   ├── repository/UserRepository.java
│   │   └── service/UserDomainService.java
│   └── infrastructure/                  # 基础设施层
│       ├── controller/UserController.java
│       └── repository/UserRepositoryImpl.java
├── order/                               # 订单领域（结构同上）
└── inventory/                           # 库存领域（结构同上）
```

---

## DDD 领域驱动开发核心要点

### 一、核心概念

| 概念 | 定义 | 说明 |
|-----|------|------|
| **领域（Domain）** | 业务问题所在的领域空间 | 如用户领域、订单领域、库存领域 |
| **子域（Subdomain）** | 领域内的细分问题空间 | 每个子域应是独立的业务边界 |
| **限界上下文（Bounded Context）** | 领域模型的边界，定义模型的语义和适用性 | 每个子域对应一个限界上下文 |
| **聚合（Aggregate）** | 一组关联对象的集合，作为数据修改的单元 | 有一个聚合根（Root）作为访问入口 |
| **聚合根（Aggregate Root）** | 聚合的核心实体，唯一的外部访问点 | 所有对聚合内对象的操作必须通过聚合根 |
| **实体（Entity）** | 具有唯一标识的对象，其生命周期内标识不变 | 如 User、Order |
| **值对象（Value Object）** | 无标识的对象，通过属性值来标识 | 如 Address、PhoneNumber、Money |
| **领域服务（Domain Service）** | 封装领域业务规则的无状态服务 | 处理跨实体的业务逻辑 |
| **仓储（Repository）** | 领域层与基础设施层之间的抽象接口 | 提供聚合的持久化操作 |
| **工厂（Factory）** | 封装复杂对象的创建逻辑 | 确保创建的对象处于有效状态 |

### 二、分层架构职责

#### 1. 领域层（Domain Layer）- 核心层
- **职责**：包含业务核心逻辑和领域模型
- **内容**：聚合、实体、值对象、领域服务、仓储接口
- **约束**：
  - 不依赖任何外部框架
  - 不包含基础设施实现
  - 纯业务逻辑，可独立测试

#### 2. 应用层（Application Layer）
- **职责**：编排领域服务，协调业务流程
- **内容**：应用服务、DTO（Command/Response）
- **约束**：
  - 不包含业务规则，只做流程编排
  - 定义事务边界
  - 协调多个领域服务或仓储

#### 3. 基础设施层（Infrastructure Layer）
- **职责**：提供技术实现支持
- **内容**：控制器、仓储实现、数据库访问、外部服务调用
- **约束**：
  - 实现领域层定义的接口
  - 处理技术细节（数据库、消息队列等）

### 三、开发规范

#### 1. 命名规范
- **聚合根**：使用名词，如 `User`、`Order`
- **值对象**：使用名词，如 `Address`、`PhoneNumber`
- **领域服务**：后缀 `DomainService`，如 `UserDomainService`
- **应用服务**：后缀 `ApplicationService`，如 `UserApplicationService`
- **仓储接口**：后缀 `Repository`，如 `UserRepository`
- **仓储实现**：后缀 `RepositoryImpl`，如 `UserRepositoryImpl`
- **DTO**：前缀 `Command`/`Response`，如 `CreateUserCommand`、`UserResponse`

#### 2. 设计原则

**单一职责原则**
- 每个类只负责一个功能

**开闭原则**
- 对扩展开放，对修改关闭

**里氏替换原则**
- 子类能替换父类使用

**依赖倒置原则**
- 依赖抽象而非具体实现
- 高层模块不依赖低层模块

**接口隔离原则**
- 使用细粒度接口

#### 3. 业务规则实现规范

- **业务规则必须放在领域层**
- 实体应封装自己的业务逻辑
- 跨实体的业务逻辑放在领域服务
- 避免在应用层或控制器中编写业务规则

#### 4. 事务管理

- 事务边界定义在应用层
- 使用 `@Transactional` 注解
- 避免跨多个聚合的事务，考虑使用最终一致性

#### 5. DTO 转换

- 应用层负责 DTO 与领域模型的转换
- 使用 `Mapper` 工具（如 MapStruct）
- 避免在领域层中出现 DTO

#### 6. 错误处理

- 领域层抛出领域异常
- 基础设施层统一处理异常并返回标准响应
- 使用 `GlobalExceptionHandler` 统一处理

### 四、实践指南

#### 1. 聚合设计
- 聚合应尽可能小，保持职责单一
- 聚合内部强一致性，聚合之间最终一致性
- 避免跨聚合的直接引用，使用标识引用

#### 2. 仓储使用
- 仓储只返回聚合根
- 仓储方法名应使用领域语言
- 避免在仓储中编写复杂查询逻辑

#### 3. 服务调用
- 应用服务调用领域服务和仓储
- 领域服务之间可以相互调用
- 避免循环依赖

#### 4. 测试策略
- 单元测试重点测试领域层
- 集成测试验证各层协作
- 使用 Mock 隔离外部依赖

### 五、代码示例结构

```
domain/
├── model/
│   ├── aggregate/
│   │   └── User.java          # 聚合根
│   └── valueobject/
│       └── Address.java       # 值对象
├── repository/
│   └── UserRepository.java    # 仓储接口
└── service/
    └── UserDomainService.java # 领域服务

application/
├── dto/
│   ├── command/
│   │   └── CreateUserCommand.java
│   └── response/
│       └── UserResponse.java
└── service/
    └── UserApplicationService.java

infrastructure/
├── controller/
│   └── UserController.java    # REST API
└── repository/
    └── UserRepositoryImpl.java # 仓储实现
```
