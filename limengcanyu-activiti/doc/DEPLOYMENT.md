# Spring Boot + Activiti 工作流项目部署文档

## 目录
1. [环境要求](#环境要求)
2. [快速部署](#快速部署)
3. [详细部署步骤](#详细部署步骤)
4. [项目验证](#项目验证)
5. [测试执行](#测试执行)
6. [常见问题及解决方案](#常见问题及解决方案)
7. [性能优化建议](#性能优化建议)

---

## 环境要求

### 必需环境
- **Java**: JDK 17 或更高版本
- **Maven**: 3.6.0+ (或使用项目内置的 Maven Wrapper)
- **内存**: 至少 2GB 可用内存
- **端口**: 8080 端口未被占用

### 可选环境
- **MySQL**: 5.7+ (生产环境推荐)
- **Git**: 用于版本控制
- **IDE**: IntelliJ IDEA 或 Eclipse

### 环境检查命令
```bash
# 检查Java版本
java -version

# 检查Maven版本
mvn --version

# 检查端口占用
lsof -i :8080
```

---

## 快速部署

### 一键启动（推荐）
```bash
# 克隆项目（如果需要）
git clone <repository-url>
cd springboot-activiti-demo

# 执行一键启动脚本
chmod +x start.sh
./start.sh
```

### 手动启动
```bash
# 编译项目
./mvnw clean compile

# 启动应用
./mvnw spring-boot:run
```

---

## 详细部署步骤

### 1. 环境准备
```bash
# 1.1 确保Java环境
java -version
# 预期输出: openjdk version "17.x.x" 或更高

# 1.2 进入项目目录
cd /path/to/springboot-activiti-demo

# 1.3 检查Maven Wrapper
ls -la mvnw*
# 确保 mvnw 文件存在且有执行权限
```

### 2. 项目编译
```bash
# 2.1 清理项目
./mvnw clean

# 2.2 编译项目
./mvnw compile

# 2.3 运行测试（可选）
./mvnw test
```

### 3. 流程部署
```bash
# 3.1 启动应用
./mvnw spring-boot:run &

# 3.2 等待应用启动完成（约30-60秒）
# 检查应用状态
curl -u admin:admin123 http://localhost:8080/actuator/health

# 3.3 部署所有流程定义
chmod +x deploy-processes.sh
./deploy-processes.sh
```

### 4. 验证部署
```bash
# 4.1 检查流程定义
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions"

# 4.2 检查应用健康状态
curl -u admin:admin123 "http://localhost:8080/actuator/health"
```

---

## 项目验证

### 验证清单
- [ ] 应用成功启动 (端口 8080)
- [ ] 健康检查正常
- [ ] 流程定义已部署
- [ ] API接口可访问
- [ ] H2数据库控制台可用

### 验证命令
```bash
# 1. 应用状态检查
curl -f -u admin:admin123 http://localhost:8080/actuator/health

# 2. 流程定义检查
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions" | grep -c "leaveRequest"

# 3. 任务API检查
curl -u admin:admin123 "http://localhost:8080/api/tasks"

# 4. H2控制台访问
open http://localhost:8080/h2-console
```

---

## 测试执行

### 自动化测试
```bash
# 1. 单元测试
./mvnw test

# 2. 集成测试
./mvnw verify

# 3. 应用功能测试
chmod +x test-app.sh
./test-app.sh
```

### 手动测试步骤

#### 1. 请假流程测试
```bash
# 1.1 提交请假申请
curl -u admin:admin123 -X POST "http://localhost:8080/api/business/leave/apply" \
  -H "Content-Type: application/json" \
  -d '{"applicant":"张三","reason":"年假","days":3}'

# 1.2 查看生成的任务
curl -u admin:admin123 "http://localhost:8080/api/tasks"

# 1.3 完成任务（示例）
curl -u admin:admin123 -X POST "http://localhost:8080/api/tasks/{taskId}/complete" \
  -H "Content-Type: application/json" \
  -d '{"approved":true,"comment":"同意请假"}'
```

#### 2. 采购流程测试
```bash
# 2.1 提交采购申请
curl -u admin:admin123 -X POST "http://localhost:8080/api/business/purchase/apply" \
  -H "Content-Type: application/json" \
  -d '{"requester":"李四","item":"办公用品","amount":1000,"reason":"日常办公需要"}'

# 2.2 查看采购任务
curl -u admin:admin123 "http://localhost:8080/api/tasks"
```

### 测试报告
测试执行后，检查以下指标：
- API响应时间 < 1秒
- 流程启动成功率 100%
- 任务分配准确率 100%
- 错误处理覆盖率 > 95%

---

## 常见问题及解决方案

### 1. 启动相关问题

#### 问题：端口 8080 被占用
```bash
# 症状
Error starting ApplicationContext. Unable to start embedded Tomcat server
Port 8080 was already in use

# 解决方案
# 方法1: 杀死占用进程
lsof -ti:8080 | xargs kill -9

# 方法2: 修改端口
echo "server.port=8081" >> src/main/resources/application.yml
```

#### 问题：Java版本不兼容
```bash
# 症状
Error: A JRE installation was not found

# 解决方案
# 安装 JDK 17
brew install openjdk@17
export JAVA_HOME=/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

#### 问题：Maven Wrapper 权限问题
```bash
# 症状
Permission denied: ./mvnw

# 解决方案
chmod +x mvnw
```

### 2. 编译相关问题

#### 问题：依赖下载失败
```bash
# 症状
Could not resolve dependencies for project

# 解决方案
# 清理Maven本地仓库
rm -rf ~/.m2/repository
./mvnw clean compile
```

#### 问题：编译错误
```bash
# 症状
Compilation failure

# 解决方案
# 检查Java版本和Maven版本
java -version
mvn --version

# 强制重新编译
./mvnw clean compile -U
```

### 3. 流程部署问题

#### 问题：流程定义未找到
```bash
# 症状
{"success":false,"message":"no processes deployed with key 'purchaseRequest'"}

# 解决方案1: 手动部署流程
curl -u admin:admin123 -X POST "http://localhost:8080/api/workflow/deploy?processName=purchase-request.bpmn20.xml"

# 解决方案2: 执行部署脚本
./deploy-processes.sh
```

#### 问题：BPMN XML 解析错误
```bash
# 症状
org.xml.sax.SAXParseException: The reference to entity "xxx" must end with the ';' delimiter

# 解决方案: 修正XML实体引用
# 将 && 改为 &amp;&amp;
# 将 < 改为 &lt;
# 将 > 改为 &gt;
# 将 " 改为 &quot;
```

### 4. API相关问题

#### 问题：404 Not Found
```bash
# 症状
HTTP/1.1 404 Not Found

# 解决方案: 检查控制器映射
# 确保 @RequestMapping 路径正确
# 检查 Controller 类是否被 @ComponentScan 扫描到
```

#### 问题：JSON 序列化失败
```bash
# 症状
Could not write JSON: could not initialize proxy

# 解决方案: 使用 DTO 对象
# 创建对应的 DTO 类
# 在 Controller 中转换 Entity 为 DTO
```

### 5. 数据库相关问题

#### 问题：H2 数据库连接失败
```bash
# 症状
Database "/mem:activiti" not found

# 解决方案: 检查数据源配置
spring:
  datasource:
    url: jdbc:h2:mem:activiti;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```

#### 问题：表不存在
```bash
# 症状
Table "ACT_RE_DEPLOYMENT" doesn't exist

# 解决方案: 启用自动建表
activiti:
  database-schema-update: true
```

### 6. 认证授权问题

#### 问题：401 Unauthorized
```bash
# 症状
HTTP/1.1 401 Unauthorized

# 解决方案: 检查认证信息
# 确保使用正确的用户名密码: admin:admin123
curl -u admin:admin123 http://localhost:8080/api/xxx
```

### 7. 性能问题

#### 问题：应用启动缓慢
```bash
# 解决方案
# 1. 增加JVM内存
export MAVEN_OPTS="-Xmx2048m -Xms1024m"

# 2. 禁用不必要的自动配置
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
```

---

## 性能优化建议

### 1. JVM 优化
```bash
# 在 start.sh 中添加JVM参数
export MAVEN_OPTS="-Xmx2048m -Xms1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### 2. 数据库优化
```yaml
# application.yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        cache:
          use_second_level_cache: true
```

### 3. 连接池优化
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
```

### 4. 监控配置
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

---

## 部署检查清单

### 部署前检查
- [ ] Java 17+ 已安装
- [ ] 端口 8080 可用
- [ ] 磁盘空间充足 (>1GB)
- [ ] 网络连接正常

### 部署后检查
- [ ] 应用启动成功
- [ ] 健康检查通过
- [ ] 所有流程定义已部署
- [ ] API 接口响应正常
- [ ] 日志无严重错误

### 生产环境额外检查
- [ ] 数据库连接配置正确
- [ ] 日志级别设置为 INFO
- [ ] 敏感信息已脱敏
- [ ] 监控系统已配置
- [ ] 备份策略已制定

---

## 联系支持

如果遇到文档中未涉及的问题，请：

1. 检查应用日志：`tail -f logs/spring.log`
2. 查看详细错误信息
3. 参考 [TROUBLESHOOTING.md](TROUBLESHOOTING.md) 文档
4. 提交 Issue 并提供详细的错误日志

---

**最后更新时间**: 2024-08-24
**文档版本**: v1.0.0