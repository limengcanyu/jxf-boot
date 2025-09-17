项目结构 (Maven):
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── securitydemo
│   │               ├── SecurityDemoApplication.java
│   │               ├── config
│   │               │   ├── CorsConfig.java
│   │               │   ├── MybatisPlusConfig.java
│   │               │   └── SecurityConfig.java
│   │               ├── controller
│   │               │   ├── AuthController.java
│   │               │   └── TestController.java
│   │               ├── entity
│   │               │   ├── Role.java
│   │               │   └── User.java
│   │               ├── exception
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   └── UserNotFoundException.java
│   │               ├── mapper
│   │               │   ├── RoleMapper.java
│   │               │   └── UserMapper.java
│   │               ├── service
│   │               │   ├── impl
│   │               │   │   ├── RoleServiceImpl.java
│   │               │   │   └── UserServiceImpl.java
│   │               │   ├── RoleService.java
│   │               │   └── UserService.java
│   │               └── util
│   │                   └── JwtUtil.java
│   └── resources
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── db
│           └── schema.sql
└── test
└── java
└── com
└── example
└── securitydemo
└── SecurityDemoApplicationTests.java

如何运行
* 确保环境: 安装JDK 17+, Maven 3.x+
* 创建项目: 使用IDE (如IntelliJ IDEA) 或 start.spring.io 创建一个Maven项目，使用上述 pom.xml 依赖。
* 复制代码: 将以上代码片段复制到对应的目录和文件中。
* 构建: 在项目根目录下运行 mvn clean install。
* 运行: 运行 SecurityDemoApplication 类的 main 方法，或使用 mvn spring-boot:run。
* 测试:
  * 访问 http://localhost:8080/h2-console (使用JDBC URL jdbc:h2:mem:testdb) 可以查看H2数据库。
  * 使用API工具 (如Postman) 测试 /api/auth/register 和 /api/auth/login 接口。
  * 使用获取到的JWT Token测试 /api/test/user 和 /api/test/admin 接口。
这个项目提供了一个坚实的基础，您可以根据具体业务需求进行扩展，例如添加更复杂的RBAC模型、OAuth2集成、Redis缓存Token等。

