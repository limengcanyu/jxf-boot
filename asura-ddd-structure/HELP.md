### 项目结构

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

