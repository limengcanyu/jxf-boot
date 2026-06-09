# Asura RESTful API 开发指南

## 一、RESTful API 设计原则

### 1. 使用合适的 HTTP 方法

| HTTP 方法 | 用途 | 幂等性 |
|-----------|------|--------|
| **GET** | 查询资源 | 是 |
| **POST** | 创建资源 | 否 |
| **PUT** | 更新资源（全量） | 是 |
| **PATCH** | 更新资源（部分） | 视实现而定 |
| **DELETE** | 删除资源 | 是 |

### 2. 资源命名规范

- **使用名词**：资源应该用名词表示，而非动词
  - ✅ `/users` (获取用户列表)
  - ✅ `/users/{id}` (获取单个用户)
  - ❌ `/getUser`
  - ❌ `/createUser`

- **使用复数形式**：保持一致性
  - ✅ `/users`
  - ✅ `/orders`

- **层级关系**：使用嵌套表示资源关系
  - ✅ `/users/{userId}/orders` (获取用户的订单)

### 3. 状态码规范

| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| **200** | OK | 请求成功 |
| **201** | Created | 资源创建成功 |
| **204** | No Content | 删除成功，无返回内容 |
| **400** | Bad Request | 请求参数错误 |
| **401** | Unauthorized | 未授权 |
| **403** | Forbidden | 禁止访问 |
| **404** | Not Found | 资源不存在 |
| **500** | Internal Server Error | 服务器内部错误 |

### 4. 统一响应格式

所有响应应使用统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1779784435950
}
```

### 5. 错误处理

- 提供清晰的错误信息
- 区分业务错误和系统错误
- 避免暴露内部实现细节

---

## 二、开发注意事项

### 1. 参数校验

- 使用 `@Valid` 和 `@Validated` 进行参数校验
- 使用 Jakarta Validation 注解：`@NotBlank`, `@Size`, `@Email`, `@Pattern` 等
- 在全局异常处理器中统一处理校验异常

### 2. 日志记录

- 使用 SLF4J 进行日志记录
- 关键业务流程记录 INFO 级别日志
- 调试信息使用 DEBUG 级别
- 异常信息使用 ERROR 级别并记录堆栈

### 3. 安全性

- 敏感信息（如密码）不应返回给客户端
- 密码应使用加密存储（如 SHA-256）
- 注意 SQL 注入防护
- 实现适当的权限控制

### 4. 性能优化

- 使用分页查询避免一次性返回大量数据
- 合理使用缓存
- 数据库查询使用索引优化

### 5. API 文档

- 使用 Knife4j/Swagger 自动生成 API 文档
- 为每个接口添加清晰的注释和说明
- 提供示例请求和响应

### 6. 版本控制

考虑在 URL 中包含版本号：
```
/api/v1/users
/api/v2/users
```

---

## 三、项目结构

```
asura-restful/
├── src/main/java/org/asura/restful/
│   ├── controller/          # REST API 控制层
│   ├── service/             # 业务逻辑层
│   │   └── impl/            # 服务实现
│   ├── repository/          # 数据访问层
│   ├── entity/              # 实体类
│   ├── dto/                 # 数据传输对象
│   │   ├── request/         # 请求DTO
│   │   └── response/        # 响应DTO
│   ├── exception/           # 异常处理
│   │   └── GlobalExceptionHandler.java
│   ├── config/              # 配置类
│   └── AsuraRestfulApplication.java
└── src/main/resources/
    └── application.yaml     # 应用配置
```

---

## 四、接口列表

### 用户管理

| 接口 | 方法 | 路径 |
|------|------|------|
| 创建用户 | POST | `/api/users` |
| 获取用户列表 | GET | `/api/users` |
| 获取用户详情 | GET | `/api/users/{id}` |
| 更新用户 | PUT | `/api/users/{id}` |
| 删除用户 | DELETE | `/api/users/{id}` |

### 订单管理

| 接口 | 方法 | 路径 |
|------|------|------|
| 创建订单 | POST | `/api/orders` |
| 获取订单列表 | GET | `/api/orders` |
| 获取订单详情 | GET | `/api/orders/{id}` |
| 更新订单状态 | PATCH | `/api/orders/{id}/status` |
| 删除订单 | DELETE | `/api/orders/{id}` |

### 健康检查

| 接口 | 方法 | 路径 |
|------|------|------|
| 健康检查 | GET | `/api/health` |
| 详细健康检查 | GET | `/api/health/detail` |

---

## 五、启动方式

### 开发环境

```bash
cd asura-restful
mvn spring-boot:run
```

### 打包部署

```bash
mvn clean package
java -jar target/asura-restful-1.0.0-SNAPSHOT.jar
```

---

## 六、访问地址

- **API 基础路径**: `http://localhost:8080/api`
- **API 文档**: `http://localhost:8080/api/doc.html`
- **健康检查**: `http://localhost:8080/api/health`