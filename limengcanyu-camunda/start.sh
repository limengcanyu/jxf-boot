#!/bin/bash

echo "=================================="
echo "Spring Boot Camunda Demo 启动脚本"
echo "=================================="

# 设置JAVA_HOME（macOS）
if [ -z "$JAVA_HOME" ]; then
    if command -v /usr/libexec/java_home &> /dev/null; then
        export JAVA_HOME=$(/usr/libexec/java_home)
        echo "设置JAVA_HOME: $JAVA_HOME"
    fi
fi

# 设置Maven参数以减少GraalVM警告
export MAVEN_OPTS="--enable-native-access=ALL-UNNAMED"

# 检查Java版本
if ! command -v java &> /dev/null; then
    echo "错误：未找到Java，请安装JDK 17或更高版本"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo "当前Java版本: $JAVA_VERSION"

if [ "$JAVA_VERSION" -lt 17 ] 2>/dev/null; then
    echo "错误：需要Java 17或更高版本，当前版本：$JAVA_VERSION"
    exit 1
fi

echo "Java环境检查通过"
echo ""

# 启动应用
echo "正在启动Spring Boot Camunda Demo..."
echo "请稍等，首次启动可能需要下载依赖包..."
echo ""

./mvnw spring-boot:run

echo ""
echo "应用启动完成！"
echo ""
echo "访问地址："
echo "- 应用主页: http://localhost:8080"
echo "- Camunda管理界面: http://localhost:8080/app (admin/admin)"
echo "- H2数据库控制台: http://localhost:8080/h2-console"
echo "- API文档: 请参考README.md"
echo ""
echo "按Ctrl+C停止应用"