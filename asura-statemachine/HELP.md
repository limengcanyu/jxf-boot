# Getting Started

### 项目结构

```d
asura-statemachine/
├── pom.xml                                          # Maven配置，使用Spring Statemachine 4.0.1
├── src/main/java/org/asura/statemachine/
│   ├── AsuraStatemachineApplication.java            # Spring Boot启动类
│   ├── config/
│   │   └── OrderStateMachineConfig.java             # 状态机配置（状态、事件、转换定义）
│   ├── controller/
│   │   └── OrderController.java                     # REST API控制器
│   ├── domain/
│   │   └── Order.java                               # 订单实体类
│   ├── dto/response/
│   │   └── OrderResponse.java                       # 响应DTO
│   ├── enums/
│   │   ├── OrderStatus.java                         # 订单状态枚举（CREATED/PAID/SHIPPED等）
│   │   └── OrderEvent.java                          # 订单事件枚举（PAY/SHIP/CANCEL等）
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java              # 全局异常处理
│   │   └── StateMachineException.java               # 自定义状态机异常
│   ├── persist/
│   │   └── InMemoryStateMachinePersister.java       # 内存状态持久化器
│   ├── service/
│   │   ├── OrderStateMachineService.java            # 服务接口
│   │   └── impl/
│   │       └── OrderStateMachineServiceImpl.java    # 服务实现（含事件重放恢复状态）
│   └── util/
│       └── OrderStatusUtil.java                     # 状态工具类
└── src/test/java/org/asura/statemachine/
    ├── AsuraStatemachineApplicationTests.java       # 基础测试
    └── service/
        └── OrderStateMachineServiceTest.java        # 状态机服务测试（9个测试用例）    
```

核心功能
1. 状态定义：OrderStatus 枚举定义了7种状态：CREATED、PAID、SHIPPED、DELIVERED、COMPLETED、CANCELLED、REFUNDED

2. 事件定义：OrderEvent 枚举定义了6种事件：PAY、SHIP、DELIVER、COMPLETE、CANCEL、REFUND

3. 状态转换规则：

* CREATED → PAID (PAY)
* PAID → SHIPPED (SHIP)
* SHIPPED → DELIVERED (DELIVER)
* DELIVERED → COMPLETED (COMPLETE)
* CREATED → CANCELLED (CANCEL)
* PAID → CANCELLED (CANCEL)
* PAID → REFUNDED (REFUND)

4. REST API：

* POST /api/orders - 创建订单
* GET /api/orders/{orderId} - 查询订单
* POST /api/orders/{orderId}/events/{event} - 触发订单事件
