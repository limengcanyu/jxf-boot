# Spring Boot + Camunda 工作流项目部署与测试文档

## 📋 目录
- [项目概述](#项目概述)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [详细部署步骤](#详细部署步骤)
- [常见问题解决](#常见问题解决)
- [API测试指南](#api测试指南)
- [管理界面使用](#管理界面使用)
- [开发调试](#开发调试)
- [故障排除](#故障排除)

---

## 📊 项目概述

这是一个基于Spring Boot 3.2.2和Camunda 7.21.0的工作流管理系统，提供完整的请假流程管理功能。

### 主要特性
- ✅ Spring Boot 3.2.2 + Camunda 7.21.0
- ✅ 完整的请假审批流程
- ✅ RESTful API接口
- ✅ Service层接口与实现分离
- ✅ 统一的API响应格式
- ✅ Web管理界面
- ✅ H2内存数据库

---

## 🔧 环境要求

### 基础环境
- **Java**: 17 或更高版本（推荐使用GraalVM）
- **Maven**: 3.6+ （项目使用Maven Wrapper，无需单独安装）
- **操作系统**: Windows/macOS/Linux
- **内存**: 最少2GB可用内存

### 端口要求
- **8080**: Spring Boot应用主端口
- **35729**: LiveReload开发工具端口（可选）

---

## 🚀 快速开始

### 1. 克隆或下载项目
```bash
# 如果从Git仓库克隆
git clone <repository-url>
cd springboot-camunda-demo

# 或者直接进入项目目录
cd /Users/rock/QoderProjects/springboot-camunda-demo
```

### 2. 检查Java环境
```bash
# 检查Java版本
java -version

# 应该显示Java 17或更高版本
# 例如: openjdk version "17.0.x" 或 "24.0.x"
```

### 3. 启动应用
```bash
# 使用Maven Wrapper启动
./mvnw spring-boot:run

# Windows用户使用
mvnw.cmd spring-boot:run
```

### 4. 验证启动
打开浏览器访问: http://localhost:8080/api/workflow/definitions

如果看到JSON响应，说明启动成功！

---

## 📋 详细部署步骤

### 步骤1: 环境验证

#### 检查Java环境
```bash
# 检查Java版本
java -version

# 检查JAVA_HOME设置
echo $JAVA_HOME

# 如果JAVA_HOME未设置，在macOS上：
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# 在Linux上：
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

#### 验证项目结构
```bash
# 确认项目文件完整性
ls -la
# 应该看到: pom.xml, mvnw, src/ 等目录

# 检查Maven Wrapper
ls -la .mvn/wrapper/
# 应该看到: maven-wrapper.properties 和 maven-wrapper.jar
```

### 步骤2: 依赖下载与编译

```bash
# 清理并编译项目
./mvnw clean compile

# 如果遇到网络问题，可以设置镜像
./mvnw clean compile -Dmaven.repo.local=~/.m2/repository
```

### 步骤3: 启动应用

#### 标准启动
```bash
# 启动开发服务器
./mvnw spring-boot:run
```

#### 后台启动
```bash
# 后台运行
nohup ./mvnw spring-boot:run > app.log 2>&1 &

# 查看日志
tail -f app.log
```

#### 指定端口启动
```bash
# 如果8080端口被占用，可以指定其他端口
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 步骤4: 验证部署

#### 健康检查
```bash
# 检查应用状态
curl http://localhost:8080/api/workflow/definitions

# 期望响应: JSON格式的流程定义列表
```

#### 功能验证
```bash
# 运行完整测试
./execute-workflow-demo.sh

# 或运行简化测试
./simple-test-api.sh
```

---

## 🔧 常见问题解决

### 问题1: 端口被占用

**现象**: 启动时看到错误 "Port 8080 was already in use"

**解决方案**:

```bash
# 方法1: 查找并终止占用进程
lsof -ti :8080
# 获得进程ID，例如: 12345

kill -9 12345

# 方法2: 一键终止8080端口进程
lsof -ti :8080 | xargs kill -9

# 方法3: 使用其他端口
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

**Windows用户**:
```cmd
# 查找占用进程
netstat -ano | findstr :8080

# 终止进程（替换PID为实际进程ID）
taskkill /PID <PID> /F
```

### 问题2: Java版本不兼容

**现象**: 
```
java.lang.UnsupportedClassVersionError
或
Unsupported major.minor version
```

**解决方案**:
```bash
# 检查当前Java版本
java -version
javac -version

# 如果版本低于17，需要安装Java 17+
# macOS使用Homebrew:
brew install openjdk@17

# Ubuntu/Debian:
sudo apt install openjdk-17-jdk

# 设置JAVA_HOME
export JAVA_HOME=/path/to/java-17
```

### 问题3: 内存不足

**现象**: OutOfMemoryError 或应用启动缓慢

**解决方案**:
```bash
# 增加JVM内存
export MAVEN_OPTS="-Xmx2g -Xms1g"
./mvnw spring-boot:run

# 或者直接指定
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx2g -Xms1g"
```

### 问题4: 网络连接问题

**现象**: 依赖下载失败或连接超时

**解决方案**:
```bash
# 使用阿里云镜像 (在 ~/.m2/settings.xml 中配置)
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>

# 或者使用代理
./mvnw spring-boot:run -Dhttp.proxyHost=proxy.company.com -Dhttp.proxyPort=8080
```

### 问题5: 权限问题

**现象**: 无法执行mvnw脚本

**解决方案**:
```bash
# 给予执行权限
chmod +x mvnw

# 如果仍有问题，检查文件所有者
ls -la mvnw
chown $(whoami) mvnw
```

### 问题6: 数据库连接问题

**现象**: H2数据库连接失败

**解决方案**:
```bash
# 检查H2控制台
curl http://localhost:8080/h2-console

# 如果无法访问，检查应用配置
cat src/main/resources/application.yml

# 重置H2数据库（删除临时文件）
rm -rf ~/camunda*
```

---

## 🧪 API测试指南

### 自动化测试

#### 完整功能测试
```bash
# 运行完整的工作流演示
./execute-workflow-demo.sh

# 这个脚本会：
# 1. 检查应用状态
# 2. 启动请假流程
# 3. 查看待办任务
# 4. 完成任务
# 5. 验证流程状态
```

#### 快速测试
```bash
# 运行简化测试
./simple-test-api.sh

# 这个脚本提供更清晰的输出格式
```

#### 基础连通性测试
```bash
# 检查API可用性
curl http://localhost:8080/api/workflow/definitions

# 期望返回: 包含流程定义的JSON
```

### 手动API测试

#### 1. 启动请假流程
```bash
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{
    "applicant": "张三",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "家庭聚会",
    "phone": "13800138000",
    "urgency": 2
  }'
```

**期望响应**:
```json
{
  "code": 200,
  "message": "请假流程启动成功",
  "data": {
    "id": "process-instance-id",
    "processDefinitionKey": "leave-process",
    ...
  }
}
```

#### 2. 查看待办任务
```bash
# 获取所有任务
curl http://localhost:8080/api/workflow/tasks

# 获取特定用户的任务
curl http://localhost:8080/api/workflow/tasks/assignee/manager
```

#### 3. 完成任务
```bash
# 先获取任务ID，然后完成任务
curl -X POST http://localhost:8080/api/workflow/tasks/{TASK_ID}/complete \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "approved": true,
      "comment": "批准请假申请"
    },
    "comment": "经理审批通过"
  }'
```

### API文档

#### 核心API端点

| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/workflow/definitions` | 获取流程定义 |
| POST | `/api/workflow/instances/leave/start` | 启动请假流程 |
| GET | `/api/workflow/tasks` | 获取所有任务 |
| GET | `/api/workflow/tasks/assignee/{user}` | 获取用户任务 |
| POST | `/api/workflow/tasks/{id}/complete` | 完成任务 |
| GET | `/api/workflow/instances` | 获取流程实例 |
| GET | `/api/workflow/history/tasks` | 获取历史任务 |

#### 请求格式

**启动请假流程请求体**:
```json
{
  "applicant": "申请人姓名",
  "leaveType": "请假类型(年假/病假/事假)",
  "startDate": "开始日期(YYYY-MM-DD)",
  "endDate": "结束日期(YYYY-MM-DD)",
  "days": 请假天数(数字),
  "reason": "请假原因",
  "phone": "联系电话",
  "urgency": 紧急程度(1-5)
}
```

**完成任务请求体**:
```json
{
  "variables": {
    "approved": true/false,
    "comment": "审批意见",
    "rejectReason": "拒绝原因(可选)"
  },
  "comment": "任务完成备注"
}
```

#### 响应格式

所有API都使用统一的响应格式：
```json
{
  "code": 200,
  "message": "操作结果信息",
  "data": "具体数据内容",
  "timestamp": 1234567890123
}
```

---

## 🖥️ 管理界面使用

### Camunda管理界面

#### 访问地址
- **URL**: http://localhost:8080/camunda
- **用户名**: admin
- **密码**: admin

#### 主要功能

1. **Tasklist (任务列表)**
   - 查看个人待办任务
   - 完成任务操作
   - 任务历史记录

2. **Cockpit (驾驶舱)**
   - 流程实例监控
   - 流程定义管理
   - 性能统计

3. **Admin (管理)**
   - 用户管理
   - 组管理
   - 权限配置

### H2数据库控制台

#### 访问地址
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:camunda
- **用户名**: sa
- **密码**: (留空)

#### 常用查询
```sql
-- 查看所有流程实例
SELECT * FROM ACT_RU_EXECUTION;

-- 查看所有任务
SELECT * FROM ACT_RU_TASK;

-- 查看流程定义
SELECT * FROM ACT_RE_PROCDEF;

-- 查看历史任务
SELECT * FROM ACT_HI_TASKINST;
```

---

## 🛠️ 开发调试

### 开发模式启动

```bash
# 启用开发工具和热重载
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 开启调试日志
./mvnw spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.example=DEBUG"
```

### 代码修改和热重载

1. **修改Java代码**: 自动重启应用
2. **修改静态资源**: 无需重启
3. **修改配置文件**: 需要手动重启

### IDE调试配置

#### IntelliJ IDEA
1. 导入Maven项目
2. 设置JDK 17
3. 运行CamundaDemoApplication.main()

#### VSCode
1. 安装Java扩展包
2. 导入项目
3. 配置launch.json:
```json
{
  "type": "java",
  "name": "Debug Camunda Demo",
  "request": "launch",
  "mainClass": "com.example.camundademo.CamundaDemoApplication",
  "vmArgs": "-Dspring.profiles.active=dev"
}
```

---

## 🔍 故障排除

### 日志查看

#### 应用日志
```bash
# 实时查看日志
./mvnw spring-boot:run | tee app.log

# 后台运行时查看日志
tail -f app.log

# 查看特定级别日志
grep ERROR app.log
grep WARN app.log
```

#### 常见错误信息

1. **ClassNotFoundException**
   - 检查依赖是否正确
   - 清理并重新编译: `./mvnw clean compile`

2. **DataAccessException**
   - 检查数据库连接
   - 重启应用重新初始化数据库

3. **BpmnParseException**
   - 检查BPMN文件语法
   - 验证流程定义是否正确

### 性能优化

#### JVM参数调优
```bash
# 生产环境推荐配置
export MAVEN_OPTS="-Xmx4g -Xms2g -XX:+UseG1GC -XX:+UseStringDeduplication"
./mvnw spring-boot:run
```

#### 应用配置优化
```yaml
# application.yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # 生产环境使用
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

camunda:
  bpm:
    job-executor:
      core-pool-size: 5
      max-pool-size: 20
```

### 监控和诊断

#### 健康检查端点
```bash
# 应用健康状态
curl http://localhost:8080/actuator/health

# JVM信息
curl http://localhost:8080/actuator/info

# 指标信息
curl http://localhost:8080/actuator/metrics
```

#### 数据库状态检查
```sql
-- 检查活跃流程实例数量
SELECT COUNT(*) FROM ACT_RU_EXECUTION WHERE PARENT_ID_ IS NULL;

-- 检查待办任务数量
SELECT COUNT(*) FROM ACT_RU_TASK;

-- 检查最近的错误
SELECT * FROM ACT_GE_BYTEARRAY WHERE NAME_ LIKE '%error%';
```

---

## 📚 参考资源

### 官方文档
- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [Camunda 文档](https://docs.camunda.org/)
- [Maven 文档](https://maven.apache.org/guides/)

### 项目相关
- **项目结构**: 遵循标准Spring Boot项目结构
- **编码规范**: Java 17标准，使用Spring Boot最佳实践
- **数据库**: H2内存数据库（开发），支持MySQL（生产）

### 常用命令速查

```bash
# 启动应用
./mvnw spring-boot:run

# 清理编译
./mvnw clean compile

# 运行测试
./mvnw test

# 打包应用
./mvnw package

# 检查端口占用
lsof -ti :8080

# 终止端口进程
lsof -ti :8080 | xargs kill -9

# 查看Java版本
java -version

# 检查应用状态
curl http://localhost:8080/api/workflow/definitions
```

---

## 📝 更新日志

### v1.0.0 (2024-08-24)
- ✅ 初始版本发布
- ✅ Spring Boot 3.2.2 + Camunda 7.21.0
- ✅ 完整的请假流程
- ✅ RESTful API
- ✅ 管理界面
- ✅ 完整测试脚本

---

**📞 技术支持**: 如果遇到问题，请检查此文档中的故障排除章节，或查看项目日志文件。