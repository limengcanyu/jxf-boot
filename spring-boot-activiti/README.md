# Spring Boot + Activiti 工作流管理系统

## 项目简介

这是一个基于 Spring Boot 3.2.2 和 Activiti 7.19.0 构建的企业级工作流管理系统，提供了完整的流程定义、流程实例管理、任务分配和业务流程自动化功能。

## 快速开始

### 一键启动
```bash
git clone <repository-url>
cd springboot-activiti-demo
chmod +x start.sh
./start.sh
```

### 访问系统
- **主应用**: http://localhost:8080
- **H2控制台**: http://localhost:8080/h2-console
- **健康检查**: http://localhost:8080/actuator/health

### 默认账户
- **管理员**: admin/admin123
- **普通用户**: user/user123  
- **经理**: manager/manager123

## 系统架构

```
┌─────────────────────────────────────────────┐
│                Web Layer                    │
├─────────────────────────────────────────────┤
│  WorkflowController │ TaskController        │
│  BusinessProcessController                  │ 
├─────────────────────────────────────────────┤
│                Service Layer                │
├─────────────────────────────────────────────┤
│  WorkflowService    │ TaskService           │
│  BusinessProcessService                     │
├─────────────────────────────────────────────┤
│               Activiti Engine               │
├─────────────────────────────────────────────┤
│                Data Layer                   │
├─────────────────────────────────────────────┤
│  H2 Database (开发) │ MySQL (生产)          │
└─────────────────────────────────────────────┘
```

## 核心功能

### 1. 工作流管理
- **流程定义部署**: 支持 BPMN 2.0 标准
- **流程实例管理**: 启动、暂停、恢复、删除
- **流程变量管理**: 动态设置和获取流程数据
- **流程监控**: 实时查看流程执行状态

### 2. 任务管理
- **任务分配**: 基于角色的自动任务分配
- **任务查询**: 支持多条件任务检索
- **任务处理**: 完成、委派、转办任务
- **任务监控**: 任务执行时间和状态追踪

### 3. 业务流程
- **请假流程**: 员工请假申请和审批
- **采购流程**: 采购申请和多级审批
- **自定义流程**: 支持扩展其他业务流程

## 技术栈

### 后端技术
- **Spring Boot**: 3.2.2
- **Activiti**: 7.19.0 
- **Spring Security**: 认证和授权
- **Spring Data JPA**: 数据访问层
- **H2 Database**: 内存数据库 (开发/测试)
- **MySQL**: 关系数据库 (生产环境)

### 构建工具
- **Maven**: 3.6+
- **Java**: JDK 17+

## API 接口

### 工作流管理 API

#### 部署流程
```http
POST /api/workflow/deploy?processName=leave-request.bpmn20.xml
Authorization: Basic admin:admin123

Response:
{
  "success": true,
  "message": "流程部署成功",
  "deploymentId": "1001"
}
```

#### 获取流程定义
```http
GET /api/workflow/definitions
Authorization: Basic admin:admin123

Response:
[
  {
    "id": "leaveRequest:1:1004",
    "key": "leaveRequest", 
    "name": "请假申请流程",
    "version": 1
  }
]
```

#### 启动流程实例
```http
POST /api/workflow/start/leaveRequest
Content-Type: application/json
Authorization: Basic admin:admin123

{
  "applicant": "张三",
  "reason": "年假", 
  "days": 3
}

Response:
{
  "success": true,
  "processInstanceId": "2501"
}
```

### 任务管理 API

#### 获取任务列表
```http
GET /api/tasks
Authorization: Basic admin:admin123

Response:
[
  {
    "taskId": "3001",
    "taskName": "经理审批",
    "assignee": "manager",
    "processInstanceId": "2501"
  }
]
```

#### 完成任务
```http
POST /api/tasks/3001/complete
Content-Type: application/json
Authorization: Basic admin:admin123

{
  "approved": true,
  "comment": "同意请假申请"
}
```

### 业务流程 API

#### 请假申请
```http
POST /api/business/leave/apply
Content-Type: application/json
Authorization: Basic admin:admin123

{
  "applicant": "李四",
  "reason": "病假",
  "days": 2
}
```

#### 采购申请
```http
POST /api/business/purchase/apply
Content-Type: application/json
Authorization: Basic admin:admin123

{
  "requester": "王五",
  "item": "办公用品", 
  "amount": 1000,
  "reason": "日常办公需要"
}
```

## 数据库配置

### H2 数据库 (开发环境)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:activiti;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  h2:
    console:
      enabled: true
      path: /h2-console
```

### MySQL 数据库 (生产环境)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${DB_USERNAME:activiti_user}
    password: ${DB_PASSWORD:activiti_password}
```

## 流程定义

### 请假流程 (leave-request.bpmn20.xml)
```
开始 → 提交申请 → 经理审批 → [天数>3] → HR审批 → 结束
                    ↓ [天数≤3]
                  → 自动通过 → 结束
```

### 采购流程 (purchase-request.bpmn20.xml)  
```
开始 → 提交申请 → 部门审批 → [金额>5000] → 财务审批 → [金额>20000] → CEO审批 → 结束
                     ↓ [金额≤5000]                ↓ [金额≤20000]
                   → 自动通过 → 结束              → 结束
```

## 项目结构

```
springboot-activiti-demo/
├── src/main/java/com/example/workflow/
│   ├── controller/          # 控制器层
│   │   ├── WorkflowController.java
│   │   ├── TaskController.java
│   │   └── BusinessProcessController.java
│   ├── service/            # 业务逻辑层
│   │   ├── IWorkflowService.java
│   │   ├── WorkflowServiceImpl.java
│   │   ├── ITaskService.java
│   │   └── TaskServiceImpl.java
│   ├── dto/                # 数据传输对象
│   │   ├── ProcessDefinitionDTO.java
│   │   └── TaskResponseDTO.java
│   ├── config/             # 配置类
│   │   ├── ActivitiConfig.java
│   │   └── SecurityConfig.java
│   └── WorkflowDemoApplication.java
├── src/main/resources/
│   ├── processes/          # BPMN流程定义
│   │   ├── leave-request.bpmn20.xml
│   │   └── purchase-request.bpmn20.xml
│   └── application.yml     # 应用配置
├── start.sh               # 启动脚本
├── deploy-processes.sh    # 流程部署脚本
├── test-app.sh           # 测试脚本
└── docs/                 # 文档目录
    ├── DEPLOYMENT.md     # 部署文档
    └── TROUBLESHOOTING.md # 故障排除
```

## 开发指南

### 添加新的业务流程

1. **创建 BPMN 文件**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">
  <process id="myProcess" name="我的流程">
    <!-- 流程定义 -->
  </process>
</definitions>
```

2. **创建控制器方法**
```java
@PostMapping("/my-process/apply")
public ResponseEntity<Map<String, Object>> applyMyProcess(@RequestBody MyProcessRequest request) {
    // 业务逻辑
}
```

3. **部署流程**
```bash
curl -u admin:admin123 -X POST "http://localhost:8080/api/workflow/deploy?processName=my-process.bpmn20.xml"
```

### 自定义任务监听器
```java
@Component
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        // 任务监听逻辑
    }
}
```

## 测试

### 运行单元测试
```bash
./mvnw test
```

### 运行集成测试
```bash
./mvnw verify
```

### 运行应用测试
```bash
chmod +x test-app.sh
./test-app.sh
```

## 监控和日志

### 健康检查
```bash
curl -u admin:admin123 http://localhost:8080/actuator/health
```

### 应用指标
```bash
curl -u admin:admin123 http://localhost:8080/actuator/metrics
```

### 日志配置
```yaml
logging:
  level:
    com.example.workflow: DEBUG
    org.activiti: INFO
  file:
    name: logs/workflow-application.log
```

## 部署

详细部署说明请参考 [DEPLOYMENT.md](./DEPLOYMENT.md)

## 故障排除

遇到问题请参考 [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

## 贡献

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请见 [LICENSE](LICENSE) 文件

## 更新日志

### v1.0.0 (2024-08-24)
- ✅ 初始版本发布
- ✅ 基础工作流管理功能
- ✅ 请假和采购业务流程
- ✅ RESTful API 接口
- ✅ 完整的部署和测试文档