# 故障排除指南

本文档提供了 Spring Boot + Activiti 工作流系统常见问题的详细解决方案。所有问题都基于实际开发和部署经验总结。

## 目录
1. [环境相关问题](#环境相关问题)
2. [应用启动问题](#应用启动问题)
3. [流程部署问题](#流程部署问题)
4. [API接口问题](#api接口问题)
5. [数据库相关问题](#数据库相关问题)
6. [认证授权问题](#认证授权问题)
7. [性能相关问题](#性能相关问题)
8. [测试脚本问题](#测试脚本问题)

---

## 环境相关问题

### Q1: Java版本不兼容
**症状**:
```
Error: A JRE installation was not found
或
java.lang.UnsupportedClassVersionError: xxx has been compiled by a more recent version of the Java Runtime
```

**原因**: 
- 系统中没有安装Java
- Java版本低于JDK 17
- JAVA_HOME环境变量未正确设置

**解决方案**:
```bash
# 检查当前Java版本
java -version

# macOS 安装 JDK 17
brew install openjdk@17

# 设置环境变量 (添加到 ~/.zshrc 或 ~/.bash_profile)
export JAVA_HOME=/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# 重新加载环境变量
source ~/.zshrc

# 验证安装
java -version
javac -version
```

### Q2: Maven Wrapper 权限问题
**症状**:
```
Permission denied: ./mvnw
或
bash: ./mvnw: Permission denied
```

**解决方案**:
```bash
# 给予执行权限
chmod +x mvnw

# 如果文件不存在，重新下载
curl -o mvnw https://raw.githubusercontent.com/apache/maven-wrapper/master/mvnw
chmod +x mvnw
```

### Q3: 端口占用问题
**症状**:
```
Port 8080 was already in use
或
Address already in use
```

**解决方案**:
```bash
# 方法1: 查找并杀死占用进程
lsof -ti:8080 | xargs kill -9

# 方法2: 修改应用端口
echo "server.port=8081" >> src/main/resources/application.yml

# 方法3: 使用其他端口启动
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

---

## 应用启动问题

### Q4: Maven 依赖下载失败
**症状**:
```
Could not resolve dependencies for project
或
Failed to collect dependencies at xxx
```

**解决方案**:
```bash
# 1. 清理本地Maven仓库
rm -rf ~/.m2/repository

# 2. 强制更新依赖
./mvnw clean compile -U

# 3. 跳过测试编译（如果测试有问题）
./mvnw clean compile -DskipTests

# 4. 使用阿里云镜像 (修改 ~/.m2/settings.xml)
cat > ~/.m2/settings.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>aliyun maven</name>
      <url>https://maven.aliyun.com/repository/central</url>
    </mirror>
  </mirrors>
</settings>
EOF
```

### Q5: Spring Boot 启动失败
**症状**:
```
Error starting ApplicationContext
或
Failed to configure a DataSource
```

**解决方案**:
```bash
# 1. 检查配置文件语法
./mvnw validate

# 2. 查看详细错误日志
./mvnw spring-boot:run -Dspring-boot.run.arguments="--debug"

# 3. 检查 application.yml 配置
cat src/main/resources/application.yml

# 4. 验证数据源配置
spring:
  datasource:
    url: jdbc:h2:mem:activiti;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 
```

### Q6: 内存不足问题
**症状**:
```
java.lang.OutOfMemoryError: Java heap space
```

**解决方案**:
```bash
# 1. 增加JVM内存
export MAVEN_OPTS="-Xmx2048m -Xms1024m"

# 2. 在 start.sh 中设置
MAVEN_OPTS="-Xmx2048m -Xms1024m -XX:+UseG1GC" ./mvnw spring-boot:run

# 3. 使用系统环境变量
echo 'export MAVEN_OPTS="-Xmx2048m -Xms1024m"' >> ~/.zshrc
source ~/.zshrc
```

---

## 流程部署问题

### Q7: 流程定义未找到（核心问题）
**症状**:
```json
{
  "success": false,
  "message": "no processes deployed with key 'purchaseRequest'"
}
```

**原因**: 
- BPMN文件中的流程ID与Controller中使用的key不匹配
- 流程定义尚未部署到Activiti引擎
- 流程部署时发生错误但被忽略

**解决方案**:
```bash
# 1. 检查流程定义是否已部署
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions"

# 2. 手动部署缺失的流程
curl -u admin:admin123 -X POST "http://localhost:8080/api/workflow/deploy?processName=purchase-request.bpmn20.xml"

# 3. 批量部署所有流程
./deploy-processes.sh

# 4. 验证BPMN文件中的流程ID
grep 'process id=' src/main/resources/processes/*.bpmn20.xml

# 5. 确保Controller中的key与BPMN文件ID一致
# purchase-request.bpmn20.xml 中应该是:
# <process id="purchaseRequest" name="采购申请流程">
```

### Q8: BPMN XML 解析错误（实际遇到的问题）
**症状**:
```
org.xml.sax.SAXParseException: The reference to entity "xxx" must end with the ';' delimiter
```

**原因**: BPMN XML文件中包含未正确转义的特殊字符

**解决方案**:
```bash
# 修正XML实体引用
sed -i 's/&&/\&amp;\&amp;/g' src/main/resources/processes/*.bpmn20.xml
sed -i 's/<=/\&lt;=/g' src/main/resources/processes/*.bpmn20.xml  
sed -i 's/>=/\&gt;=/g' src/main/resources/processes/*.bpmn20.xml
sed -i 's/"/\&quot;/g' src/main/resources/processes/*.bpmn20.xml

# 手动修改示例:
# 错误: <conditionExpression xsi:type="tFormalExpression">${days > 3}</conditionExpression>
# 正确: <conditionExpression xsi:type="tFormalExpression">${days &gt; 3}</conditionExpression>
```

**常见XML实体引用对照表**:
| 字符 | XML实体 |
|------|---------|
| `<`  | `&lt;`  |
| `>`  | `&gt;`  |
| `&`  | `&amp;` |
| `"`  | `&quot;`|
| `'`  | `&apos;`|

### Q9: 流程部署成功但无法启动
**症状**:
```
ProcessDefinition with key 'xxx' not found
```

**解决方案**:
```bash
# 1. 检查流程定义状态
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions" | grep -A5 -B5 "suspended"

# 2. 激活被挂起的流程定义
curl -u admin:admin123 -X PUT "http://localhost:8080/api/workflow/definitions/{processDefinitionId}/activate"

# 3. 重新部署流程
curl -u admin:admin123 -X POST "http://localhost:8080/api/workflow/deploy?processName=your-process.bpmn20.xml"
```

---

## API接口问题

### Q10: 404 Not Found 错误（实际遇到的问题）
**症状**:
```
HTTP/1.1 404 Not Found
{"timestamp":"2024-08-24T10:30:00.000+00:00","status":404,"error":"Not Found","path":"/api/workflow/deploy"}
```

**原因**: 
- Controller中缺少对应的请求映射方法
- 请求路径不正确
- Controller类未被Spring扫描到

**解决方案**:
```java
// 1. 确保Controller类有正确的注解
@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {
    
    // 2. 添加缺失的方法映射
    @PostMapping("/deploy")
    public ResponseEntity<Map<String, Object>> deployProcess(@RequestParam String processName) {
        // 实现部署逻辑
    }
}

// 3. 检查ComponentScan配置
@SpringBootApplication
@ComponentScan(basePackages = "com.example.workflow")
public class WorkflowDemoApplication {
    // ...
}
```

### Q11: JSON序列化失败（实际遇到的问题）
**症状**:
```
Could not write JSON: could not initialize proxy - no Session
或  
No serializer found for class org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl
```

**原因**: 
- Activiti实体包含延迟加载的属性
- 实体类没有合适的JSON序列化配置
- Jackson配置不正确

**解决方案**:
```java
// 1. 创建DTO类避免直接序列化实体
@Component
public class ProcessDefinitionDTO {
    private String id;
    private String key;
    private String name;
    private int version;
    // ... getters and setters
}

// 2. 在Controller中使用DTO
@GetMapping("/definitions")
public ResponseEntity<List<ProcessDefinitionDTO>> getAllProcessDefinitions() {
    List<ProcessDefinition> definitions = workflowService.getAllProcessDefinitions();
    List<ProcessDefinitionDTO> dtos = definitions.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
}

// 3. 添加转换方法
private ProcessDefinitionDTO convertToDTO(ProcessDefinition definition) {
    ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
    dto.setId(definition.getId());
    dto.setKey(definition.getKey());
    dto.setName(definition.getName());
    dto.setVersion(definition.getVersion());
    return dto;
}
```

### Q12: 请求参数绑定失败
**症状**:
```
Required request parameter 'xxx' for method parameter type String is not present
```

**解决方案**:
```java
// 1. 检查参数注解
@PostMapping("/deploy")
public ResponseEntity<?> deployProcess(@RequestParam("processName") String processName) {
    // ...
}

// 2. 使用可选参数
@PostMapping("/deploy") 
public ResponseEntity<?> deployProcess(@RequestParam(value = "processName", required = false) String processName) {
    if (processName == null) {
        processName = "default-process.bpmn20.xml";
    }
    // ...
}

// 3. 使用RequestBody接收JSON
@PostMapping("/start/{processKey}")
public ResponseEntity<?> startProcess(
    @PathVariable String processKey,
    @RequestBody Map<String, Object> variables) {
    // ...
}
```

---

## 数据库相关问题

### Q13: H2数据库连接失败
**症状**:
```
Database "/mem:activiti" not found
或
Connection is broken: java.net.ConnectException
```

**解决方案**:
```yaml
# 1. 检查数据源配置
spring:
  datasource:
    url: jdbc:h2:mem:activiti;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 

# 2. 启用H2控制台
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true

# 3. 验证连接
curl http://localhost:8080/h2-console
```

### Q14: Activiti表未创建
**症状**:
```
Table "ACT_RE_DEPLOYMENT" doesn't exist
或
Unknown table 'activiti.ACT_RE_DEPLOYMENT'
```

**解决方案**:
```yaml
# 1. 启用自动建表
activiti:
  database-schema-update: true
  db-identity-used: true

# 2. JPA配置
spring:
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.H2Dialect

# 3. 手动验证表是否创建
curl -u admin:admin123 "http://localhost:8080/h2-console"
# 执行SQL: SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'ACT_%';
```

### Q15: 数据库锁定问题
**症状**:
```
Timeout trying to lock table "ACT_RU_JOB"
```

**解决方案**:
```bash
# 1. 重启应用释放锁
pkill -f "spring-boot:run"
./mvnw spring-boot:run

# 2. 使用文件数据库模式
spring:
  datasource:
    url: jdbc:h2:file:./data/activiti;AUTO_SERVER=TRUE

# 3. 配置数据库连接池
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      connection-timeout: 20000
```

---

## 认证授权问题

### Q16: 401 Unauthorized 错误
**症状**:
```
HTTP/1.1 401 Unauthorized
{"timestamp":"...","status":401,"error":"Unauthorized","path":"/api/..."}
```

**解决方案**:
```bash
# 1. 使用正确的认证信息
curl -u admin:admin123 http://localhost:8080/api/workflow/definitions

# 2. 检查用户配置
grep -r "admin\|user123" src/main/java/*/config/

# 3. 验证Security配置
curl -v -u admin:admin123 http://localhost:8080/actuator/health
```

### Q17: 403 Forbidden 错误
**症状**:
```
HTTP/1.1 403 Forbidden
Access Denied
```

**解决方案**:
```java
// 1. 检查Security配置
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic();
        return http.build();
    }
}

// 2. 添加CORS配置
@CrossOrigin(origins = "*")
@RestController
public class WorkflowController {
    // ...
}
```

---

## 性能相关问题

### Q18: 应用启动缓慢
**症状**: 应用启动时间超过2分钟

**解决方案**:
```bash
# 1. 增加JVM内存
export MAVEN_OPTS="-Xmx2048m -Xms1024m -XX:+UseG1GC"

# 2. 禁用不必要的自动配置
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration

# 3. 关闭开发工具
spring:
  devtools:
    restart:
      enabled: false

# 4. 调整日志级别
logging:
  level:
    root: WARN
    com.example.workflow: INFO
```

### Q19: API响应慢
**症状**: API请求响应时间超过5秒

**解决方案**:
```yaml
# 1. 优化数据库连接
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000

# 2. 启用缓存
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterAccess=300s

# 3. 异步处理
activiti:
  async-executor-activate: true
```

---

## 测试脚本问题

### Q20: test-app.sh 执行失败（核心问题）
**症状**:
```bash
./test-app.sh
✗ 应用程序可能未启动或端口被占用
或者各种API返回404错误
```

**原因**: 
- 应用未完全启动
- API端点缺失
- 流程定义未部署
- 请求参数不正确

**解决方案**:
```bash
# 1. 确保应用完全启动
./mvnw spring-boot:run &
sleep 60  # 等待应用完全启动

# 2. 检查应用状态
curl -f -u admin:admin123 http://localhost:8080/actuator/health

# 3. 部署所有流程
./deploy-processes.sh

# 4. 运行测试脚本
chmod +x test-app.sh
./test-app.sh

# 5. 如果脚本失败，逐个测试API
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions"
curl -u admin:admin123 "http://localhost:8080/api/tasks"
```

### Q21: curl 命令执行失败
**症状**:
```
curl: (7) Failed to connect to localhost port 8080: Connection refused
```

**解决方案**:
```bash
# 1. 检查应用是否运行
ps aux | grep spring-boot:run

# 2. 检查端口监听
netstat -an | grep 8080
# 或
lsof -i :8080

# 3. 等待应用启动
while ! curl -s http://localhost:8080/actuator/health > /dev/null; do
    echo "等待应用启动..."
    sleep 5
done
echo "应用已启动"

# 4. 使用详细输出调试
curl -v -u admin:admin123 http://localhost:8080/api/workflow/definitions
```

---

## 诊断工具和命令

### 应用状态检查
```bash
# 检查进程
ps aux | grep java | grep spring-boot

# 检查端口
lsof -i :8080

# 检查日志
tail -f logs/spring.log

# 内存使用
jstat -gc $(pgrep -f spring-boot:run)
```

### 网络诊断
```bash
# 测试连通性
curl -I http://localhost:8080/actuator/health

# 检查DNS
nslookup localhost

# 测试端口
telnet localhost 8080
```

### 数据库诊断
```bash
# H2控制台访问
open http://localhost:8080/h2-console

# 查看Activiti表
curl -u admin:admin123 "http://localhost:8080/api/workflow/definitions"

# 检查流程部署
curl -u admin:admin123 "http://localhost:8080/api/workflow/deployments"
```

---

## 预防性措施

### 1. 定期检查清单
- [ ] Java和Maven版本兼容性
- [ ] 端口可用性
- [ ] 磁盘空间充足
- [ ] 流程定义同步
- [ ] API接口功能正常

### 2. 监控配置
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,activiti
  endpoint:
    health:
      show-details: always
```

### 3. 日志配置
```yaml
logging:
  level:
    com.example.workflow: DEBUG
    org.activiti: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

---

## 获取帮助

### 1. 收集诊断信息
```bash
# 创建诊断报告
echo "=== System Information ===" > diagnosis.log
java -version >> diagnosis.log 2>&1
mvn --version >> diagnosis.log 2>&1
echo "=== Application Status ===" >> diagnosis.log
curl -s -u admin:admin123 http://localhost:8080/actuator/health >> diagnosis.log 2>&1
echo "=== Process Definitions ===" >> diagnosis.log  
curl -s -u admin:admin123 http://localhost:8080/api/workflow/definitions >> diagnosis.log 2>&1
echo "=== Recent Logs ===" >> diagnosis.log
tail -50 logs/spring.log >> diagnosis.log 2>&1
```

### 2. 常用的调试参数
```bash
# 启用调试模式
./mvnw spring-boot:run -Dspring-boot.run.arguments="--debug --trace"

# 启用Activiti调试
./mvnw spring-boot:run -Dspring-boot.run.arguments="--logging.level.org.activiti=DEBUG"
```

### 3. 报告问题时请提供
- 错误的完整堆栈跟踪
- 相关的配置文件内容
- 系统环境信息（Java版本、OS等）
- 重现问题的详细步骤
- 诊断报告文件

---

**最后更新**: 2024-08-24  
**版本**: v1.0.0

如果本文档没有涵盖您遇到的问题，请参考 [DEPLOYMENT.md](DEPLOYMENT.md) 获取更多部署相关信息。