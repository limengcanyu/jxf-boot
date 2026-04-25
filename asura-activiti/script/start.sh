#!/bin/bash

# Spring Boot + Activiti 工作流项目启动脚本

echo "=========================================="
echo "Spring Boot + Activiti 工作流项目"
echo "=========================================="

# 检查Java版本
echo "检查Java环境..."
java_version=$(java -version 2>&1 | grep -i version | awk -F'"' '{print $2}' | awk -F'.' '{print $1}')
if [ "$java_version" -lt "17" ]; then
    echo "错误: 需要Java 17或更高版本，当前版本: $java_version"
    exit 1
fi
echo "Java版本检查通过: $(java -version 2>&1 | head -1)"

# 检查Maven
echo "检查Maven环境..."
if [ -f "./mvnw" ]; then
    echo "使用Maven Wrapper: ./mvnw"
    MVN_CMD="./mvnw"
elif command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
    echo "Maven版本: $(mvn --version | head -1)"
else
    echo "错误: 未找到Maven或Maven Wrapper，正在下载Maven Wrapper..."
    # 下载Maven Wrapper
    mkdir -p .mvn/wrapper
    curl -s -o .mvn/wrapper/maven-wrapper.jar https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    
    # 创建配置文件
    cat > .mvn/wrapper/maven-wrapper.properties << EOF
distributionUrl=https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.4/apache-maven-3.9.4-bin.zip
wrapperUrl=https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
EOF
    
    # 如果mvnw不存在或没有执行权限，下载它
    if [ ! -x "./mvnw" ]; then
        curl -s -o mvnw https://raw.githubusercontent.com/apache/maven-wrapper/master/mvnw
        chmod +x mvnw
    fi
    
    MVN_CMD="./mvnw"
    echo "Maven Wrapper 安装完成"
fi

# 清理并编译项目
echo "=========================================="
echo "清理并编译项目..."
echo "=========================================="
$MVN_CMD clean compile

if [ $? -ne 0 ]; then
    echo "编译失败，请检查代码"
    exit 1
fi

echo "编译成功！"

# 运行项目
echo "=========================================="
echo "启动Spring Boot应用..."
echo "=========================================="
echo "应用将在以下地址启动:"
echo "- 主应用: http://localhost:8080"
echo "- H2控制台: http://localhost:8080/h2-console"
echo "- 健康检查: http://localhost:8080/actuator/health"
echo ""
echo "预置用户账号:"
echo "- admin/admin123 (管理员)"
echo "- user/user123 (普通用户)"
echo "- manager/manager123 (经理)"
echo ""
echo "按 Ctrl+C 停止应用"
echo "=========================================="

# 启动应用
$MVN_CMD spring-boot:run &

# 获取应用进程ID
APP_PID=$!
echo "应用进程ID: $APP_PID"

# 等待应用启动后自动部署流程
echo ""
echo "========================================="
echo "自动部署工作流程..."
echo "========================================="

# 在后台启动流程部署
(
    sleep 10  # 等待应用完全启动
    if [ -f "./deploy-processes.sh" ]; then
        ./deploy-processes.sh
    else
        echo "警告: 未找到流程部署脚本 deploy-processes.sh"
    fi
) &

# 等待应用进程
wait $APP_PID