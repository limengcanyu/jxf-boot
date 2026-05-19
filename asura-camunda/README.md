# Spring Boot Camunda 工作流示例项目

本项目是一个集成Camunda工作流引擎的Spring Boot应用示例，展示了完整的工作流开发实践。

## 项目特性

- **Spring Boot 3.2.2** - 最新版本的Spring Boot框架
- **Camunda BPM 7.21.0** - 强大的工作流引擎
- **请假流程示例** - 完整的业务流程演示
- **统一响应格式** - 标准化API响应
- **接口分离设计** - 服务层接口与实现分离
- **H2内存数据库** - 开箱即用，便于测试
- **Camunda Web应用** - 内置流程管理界面

## 技术栈

- **后端框架**: Spring Boot 3.2.2
- **工作流引擎**: Camunda BPM 7.21.0
- **数据库**: H2 (开发/测试), MySQL (生产可选)
- **安全框架**: Spring Security
- **构建工具**: Maven 3.9.5
- **Java版本**: JDK 17+

## 项目结构

```
springboot-camunda-demo/
├── src/main/java/com/example/camundademo/
│   ├── CamundaDemoApplication.java          # 主启动类
│   ├── config/                              # 配置类
│   │   ├── CamundaConfig.java              # Camunda配置
│   │   └── SecurityConfig.java             # 安全配置
│   ├── controller/                          # 控制器层
│   │   └── WorkflowController.java         # 工作流控制器
│   ├── dto/                                # 数据传输对象
│   │   ├── ApiResult.java                  # 统一响应结果
│   │   ├── ProcessDefinitionDTO.java       # 流程定义DTO
│   │   ├── ProcessInstanceDTO.java         # 流程实例DTO
│   │   ├── TaskDTO.java                    # 任务DTO
│   │   ├── LeaveRequestDTO.java            # 请假申请DTO
│   │   ├── StartProcessRequestDTO.java     # 启动流程请求DTO
│   │   └── CompleteTaskRequestDTO.java     # 完成任务请求DTO
│   ├── service/                            # 服务层
│   │   ├── IWorkflowService.java           # 工作流服务接口
│   │   ├── impl/
│   │   │   └── WorkflowServiceImpl.java    # 工作流服务实现
│   │   ├── LeaveApprovedDelegate.java      # 请假批准委托
│   │   └── LeaveRejectedDelegate.java      # 请假拒绝委托
│   └── entity/                             # 实体类（预留）
├── src/main/resources/
│   ├── processes/                          # BPMN流程文件
│   │   └── leave-process.bpmn             # 请假流程定义
│   └── application.yml                     # 应用配置
├── pom.xml                                 # Maven配置
└── README.md                               # 项目说明
```

## 🚀 快速开始指南

## 一分钟启动

```bash
# 1. 进入项目目录
cd springboot-camunda-demo

# 2. 启动应用
./mvnw spring-boot:run

# 3. 等待启动完成后，运行测试
./execute-workflow-demo.sh
```

## 🔧 如果遇到问题

### 端口被占用
```bash
# 终止占用8080端口的进程
lsof -ti :8080 | xargs kill -9

# 然后重新启动
./mvnw spring-boot:run
```

### Java版本问题
```bash
# 检查Java版本（需要17+）
java -version

# 如果版本不对，设置JAVA_HOME
export JAVA_HOME=/path/to/java-17
```

## 🌐 访问地址

- **API测试**: http://localhost:8080/api/workflow/definitions
- **管理界面**: http://localhost:8080/camunda (admin/admin)
- **数据库控制台**: http://localhost:8080/h2-console

## 📋 核心API

```bash
# 启动请假流程
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{"applicant":"张三","leaveType":"年假","days":3,"reason":"测试"}'

# 查看待办任务
curl http://localhost:8080/api/workflow/tasks

# 查看特定用户任务
curl http://localhost:8080/api/workflow/tasks/assignee/manager
```

## 📚 详细文档

查看 [DEPLOYMENT.md](doc/DEPLOYMENT.md) 获取完整的部署和故障排除指南。

## 工作流示例 - 请假流程

### 流程说明

项目包含一个完整的请假流程示例，流程步骤如下：

1. **提交申请** - 员工提交请假申请
2. **经理审批** - 经理审核请假申请
3. **天数判断** - 根据请假天数决定审批路径
   - ≤3天：经理可直接审批
   - >3天：需要总监审批
4. **总监审批** - 长期请假需总监审批
5. **系统处理** - 自动处理批准/拒绝逻辑
6. **流程结束** - 完成审批流程

### API接口

#### 启动请假流程
```http
POST /workflow/instances/leave/start
Content-Type: application/json

{
    "applicant": "张三",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "家庭聚会",
    "phone": "13800138000",
    "urgency": 2
}
```

#### 查询待办任务
```http
GET /workflow/tasks/assignee/manager
```

#### 完成任务
```http
POST /workflow/tasks/{taskId}/complete
Content-Type: application/json

{
    "variables": {
        "approved": true,
        "comment": "同意请假"
    },
    "comment": "审批通过"
}
```

## 核心功能

### 1. 流程定义管理
- 查询所有流程定义
- 根据Key查询流程定义
- 挂起/激活流程定义

### 2. 流程实例管理
- 启动流程实例
- 查询流程实例
- 删除流程实例

### 3. 任务管理
- 查询所有任务
- 根据分配人查询任务
- 根据候选用户查询任务
- 完成任务
- 分配/取消分配任务

### 4. 历史记录查询
- 查询历史流程实例
- 查询历史任务
- 流程追踪

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:camunda;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: ""
```

### Camunda配置
```yaml
camunda:
  bpm:
    admin-user:
      id: admin
      password: admin
    database:
      schema-update: true
    history-level: full
    authorization:
      enabled: false
```

## 开发指南

### 添加新的工作流程

1. **创建BPMN文件**
   - 在 `src/main/resources/processes/` 目录下创建新的 `.bpmn` 文件
   - 使用Camunda Modeler设计流程图

2. **创建服务任务委托类**
   ```java
   @Component("yourDelegate")
   public class YourDelegate implements JavaDelegate {
       @Override
       public void execute(DelegateExecution execution) throws Exception {
           // 业务逻辑处理
       }
   }
   ```

3. **扩展服务接口**
   - 在 `IWorkflowService` 接口中添加新方法
   - 在 `WorkflowServiceImpl` 中实现具体逻辑

4. **添加控制器接口**
   - 在 `WorkflowController` 中添加新的REST接口

### 自定义配置

#### 切换到MySQL数据库
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/camunda?useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

#### 启用邮件通知
```yaml
app:
  workflow:
    notification:
      enabled: true
      from: noreply@yourcompany.com
```

## 测试

### 运行单元测试
```bash
./mvnw test
```

### 手动测试流程
1. 启动应用
2. 使用Postman或curl调用API接口
3. 在Camunda Tasklist中查看和处理任务
4. 在Camunda Cockpit中监控流程执行

## 常见问题

### Q: 如何查看流程图？
A: 访问 http://localhost:8080/app，使用admin/admin登录，在Cockpit中可以查看已部署的流程图。

### Q: 如何修改数据库连接？
A: 修改 `application.yml` 中的数据源配置，并添加相应的数据库驱动依赖。

### Q: 如何添加新的用户？
A: 在 `SecurityConfig.java` 中的 `userDetailsService()` 方法中添加新用户。

### Q: 流程实例启动失败怎么办？
A: 检查BPMN文件是否正确部署，查看应用日志获取详细错误信息。

## 参考文档

- [Camunda官方文档](https://docs.camunda.org/manual/7.21/)
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [BPMN 2.0规范](https://www.omg.org/spec/BPMN/2.0/)

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 贡献

欢迎提交Issue和Pull Request来完善这个项目。

---

**联系方式**: 如有问题，请通过GitHub Issues联系。