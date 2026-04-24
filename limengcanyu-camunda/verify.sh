#!/bin/bash

echo "=================================="
echo "Spring Boot Camunda Demo 验证脚本"
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

echo ""
echo "1. 检查Java环境..."
java -version

echo ""
echo "2. 检查Maven Wrapper..."
./mvnw --version | head -5

echo ""
echo "3. 验证项目配置..."
./mvnw validate

echo ""
echo "4. 检查项目结构..."
echo "主要文件检查："
echo "- pom.xml: $(test -f pom.xml && echo '✅' || echo '❌')"
echo "- 主启动类: $(test -f src/main/java/com/example/camundademo/CamundaDemoApplication.java && echo '✅' || echo '❌')"
echo "- 配置文件: $(test -f src/main/resources/application.yml && echo '✅' || echo '❌')"
echo "- BPMN流程: $(test -f src/main/resources/processes/leave-process.bpmn && echo '✅' || echo '❌')"
echo "- 服务接口: $(test -f src/main/java/com/example/camundademo/service/IWorkflowService.java && echo '✅' || echo '❌')"
echo "- 控制器: $(test -f src/main/java/com/example/camundademo/controller/WorkflowController.java && echo '✅' || echo '❌')"

echo ""
echo "=================================="
echo "✅ 项目验证完成！"
echo "=================================="
echo ""
echo "现在可以运行以下命令启动项目："
echo "./start.sh"
echo ""
echo "或者直接使用Maven命令："
echo "./mvnw spring-boot:run"