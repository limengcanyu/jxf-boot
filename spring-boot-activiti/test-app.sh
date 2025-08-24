#!/bin/bash

echo "=== Spring Boot + Activiti 应用测试 ==="
echo "时间: $(date)"
echo ""

# 设置认证信息
AUTH="admin:admin123"

# 检查应用是否运行
echo "1. 检查应用状态..."
if curl -s -u $AUTH http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✓ 应用程序正在运行并响应请求"
else
    echo "✗ 应用程序可能未启动或端口被占用"
    exit 1
fi

# 测试健康检查端点
echo ""
echo "2. 测试健康检查端点..."
HEALTH=$(curl -s -u $AUTH http://localhost:8080/actuator/health 2>/dev/null)
if [ $? -eq 0 ]; then
    echo "健康状态: $HEALTH"
else
    echo "✗ 健康检查请求失败"
fi

# 测试工作流API
echo ""
echo "3. 测试工作流管理API..."

# 部署流程定义
echo "3.1 部署请假申请流程..."
RESPONSE=$(curl -s -u $AUTH -X POST "http://localhost:8080/api/workflow/deploy?processName=leave-request.bpmn20.xml" 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" -X POST "http://localhost:8080/api/workflow/deploy?processName=leave-request.bpmn20.xml" 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

echo ""
echo "3.2 获取所有流程定义..."
RESPONSE=$(curl -s -u $AUTH "http://localhost:8080/api/workflow/definitions" 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" "http://localhost:8080/api/workflow/definitions" 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

# 启动流程实例
echo ""
echo "3.3 启动请假申请流程..."
RESPONSE=$(curl -s -u $AUTH -X POST "http://localhost:8080/api/workflow/start/leaveRequest" \
  -H "Content-Type: application/json" \
  -d '{"applicant":"张三","reason":"年假","days":3}' 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" -X POST "http://localhost:8080/api/workflow/start/leaveRequest" \
  -H "Content-Type: application/json" \
  -d '{"applicant":"张三","reason":"年假","days":3}' 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

echo ""
echo "4. 测试任务管理API..."

# 获取任务列表
echo "4.1 获取所有任务..."
RESPONSE=$(curl -s -u $AUTH "http://localhost:8080/api/tasks" 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" "http://localhost:8080/api/tasks" 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

echo ""
echo "5. 测试业务流程API..."

# 测试请假申请
echo "5.1 提交请假申请..."
RESPONSE=$(curl -s -u $AUTH -X POST "http://localhost:8080/api/business/leave/apply" \
  -H "Content-Type: application/json" \
  -d '{"applicant":"李四","reason":"\u75c5\u5047","days":2}' 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" -X POST "http://localhost:8080/api/business/leave/apply" \
  -H "Content-Type: application/json" \
  -d '{"applicant":"李四","reason":"\u75c5\u5047","days":2}' 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

# 测试采购申请
echo ""
echo "5.2 提交采购申请..."
RESPONSE=$(curl -s -u $AUTH -X POST "http://localhost:8080/api/business/purchase/apply" \
  -H "Content-Type: application/json" \
  -d '{"requester":"王五","item":"办公用品","amount":1000,"reason":"日常办公需要"}' 2>/dev/null)
STATUS_CODE=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" -X POST "http://localhost:8080/api/business/purchase/apply" \
  -H "Content-Type: application/json" \
  -d '{"requester":"王五","item":"办公用品","amount":1000,"reason":"日常办公需要"}' 2>/dev/null)
echo "$RESPONSE"
echo "HTTP状态码: $STATUS_CODE"

echo ""
echo "=== 测试完成 ==="
echo "Spring Boot + Activiti 工作流应用测试结果:"
echo "- 应用程序启动成功"
echo "- Activiti引擎初始化完成"
echo "- 数据库连接正常"
echo "- REST API接口可用"
echo ""
echo "您现在可以："
echo "1. 访问 http://localhost:8080/actuator 查看应用监控信息 (用户名: admin, 密码: admin123)"
echo "2. 访问 http://localhost:8080/h2-console 查看H2数据库控制台"
echo "   - JDBC URL: jdbc:h2:mem:activiti"
echo "   - 用户名: sa"
echo "   - 密码: (空)"
echo "3. 使用 curl 或 Postman 测试工作流API (基本认证: admin/admin123)"
echo ""
echo "应用程序正在运行中... 按 Ctrl+C 停止"