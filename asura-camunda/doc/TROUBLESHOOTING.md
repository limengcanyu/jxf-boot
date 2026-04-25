# 🔧 故障排除快速参考

## 🚨 常见启动问题

### 1. 端口8080被占用
```
错误信息: "Port 8080 was already in use"
```

**解决方案**:
```bash
# 方法1: 一键解决
lsof -ti :8080 | xargs kill -9 && ./mvnw spring-boot:run

# 方法2: 查看并手动终止
lsof -ti :8080
kill -9 [进程ID]

# 方法3: 使用其他端口
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 2. Java版本不兼容
```
错误信息: "UnsupportedClassVersionError" 或 "Unsupported major.minor version"
```

**解决方案**:
```bash
# 检查当前版本
java -version

# 需要Java 17+，如果版本过低：
# macOS:
brew install openjdk@17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17

# Linux:
sudo apt install openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

### 3. 内存不足
```
错误信息: "OutOfMemoryError" 或启动非常慢
```

**解决方案**:
```bash
# 增加内存
export MAVEN_OPTS="-Xmx2g -Xms1g"
./mvnw spring-boot:run
```

### 4. 权限问题
```
错误信息: "Permission denied" 或无法执行mvnw
```

**解决方案**:
```bash
# 给予执行权限
chmod +x mvnw
chmod +x *.sh
```

## 🔍 快速诊断命令

```bash
# 检查Java环境
java -version
echo $JAVA_HOME

# 检查端口占用
lsof -ti :8080
netstat -tulpn | grep :8080

# 检查应用状态
curl -s http://localhost:8080/api/workflow/definitions

# 查看应用日志
tail -f app.log

# 检查进程
ps aux | grep java
```

## ⚡ 应急重启流程

```bash
# 1. 停止所有相关进程
pkill -f "spring-boot:run"
lsof -ti :8080 | xargs kill -9

# 2. 清理并重新编译
./mvnw clean compile

# 3. 重新启动
./mvnw spring-boot:run

# 4. 验证启动
sleep 10
curl http://localhost:8080/api/workflow/definitions
```

## 🌐 网络问题

### API返回401/403错误
```bash
# 检查安全配置是否正确
grep -r "api/workflow" src/main/java/

# 重启应用
./mvnw spring-boot:run
```

### 无法访问管理界面
```bash
# 检查Camunda配置
curl http://localhost:8080/camunda

# 如果不可访问，检查依赖
grep -A5 -B5 "camunda" pom.xml
```

## 📊 性能问题

### 启动缓慢
```bash
# 使用更多内存
export MAVEN_OPTS="-Xmx4g -Xms2g"

# 跳过测试启动
./mvnw spring-boot:run -DskipTests
```

### 运行时卡顿
```bash
# 检查内存使用
jps -v | grep CamundaDemoApplication

# 生成堆转储（如果需要）
jcmd [PID] GC.run_finalization
```

## 🔧 开发环境问题

### IDE无法导入项目
```bash
# 重新生成IDE文件
./mvnw clean
./mvnw idea:idea  # IntelliJ IDEA
./mvnw eclipse:eclipse  # Eclipse
```

### 热重载不工作
```bash
# 确保开发工具依赖存在
grep -A3 -B3 "devtools" pom.xml

# 重启开发服务器
```

## 🗃️ 数据库问题

### H2数据库无法访问
```bash
# 重置H2数据库
rm -rf ~/camunda*
./mvnw spring-boot:run
```

### 数据不一致
```bash
# 清理并重启
./mvnw clean
./mvnw spring-boot:run
```

## 📞 紧急联系信息

如果以上方法都无法解决问题：

1. 查看完整错误日志
2. 检查 [DEPLOYMENT.md](DEPLOYMENT.md) 详细故障排除章节
3. 确认环境配置是否符合要求
4. 尝试在干净环境中重新部署

## 🎯 验证修复

问题解决后，运行以下命令验证：

```bash
# 1. 基础连通性
curl http://localhost:8080/api/workflow/definitions

# 2. 完整功能测试
./execute-workflow-demo.sh

# 3. 管理界面测试
curl http://localhost:8080/camunda
```

**状态正常标志**:
- ✅ API返回JSON格式的流程定义
- ✅ 测试脚本执行成功
- ✅ 管理界面可以访问