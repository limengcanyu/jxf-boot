#!/bin/bash

echo "🚀 Camunda工作流API执行演示"
echo "=================================="

BASE_URL="http://localhost:8080/api/workflow"

# 检查应用是否启动
echo "📡 检查应用状态..."
if ! curl -s "$BASE_URL/definitions" > /dev/null 2>&1; then
    echo "❌ 应用未启动，请先执行: ./mvnw spring-boot:run"
    exit 1
fi
echo "✅ 应用正在运行"
echo ""

# 步骤1: 启动请假流程
echo "📝 步骤1: 启动请假流程"
echo "=================================="

LEAVE_REQUEST='{
    "applicant": "演示用户",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27", 
    "days": 3,
    "reason": "API演示测试",
    "phone": "13800138000",
    "urgency": 2
}'

echo "发送请求: POST $BASE_URL/instances/leave/start"
PROCESS_RESPONSE=$(curl -s -X POST "$BASE_URL/instances/leave/start" \
    -H "Content-Type: application/json" \
    -d "$LEAVE_REQUEST")

echo "响应结果:"
echo "$PROCESS_RESPONSE" | python3 -m json.tool

# 提取流程实例ID
PROCESS_ID=$(echo "$PROCESS_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo ""
echo "🆔 创建的流程实例ID: $PROCESS_ID"
echo ""

# 步骤2: 查看待办任务
echo "📋 步骤2: 查看待办任务"
echo "=================================="

echo "发送请求: GET $BASE_URL/tasks"
TASKS_RESPONSE=$(curl -s "$BASE_URL/tasks")

echo "响应结果:"
echo "$TASKS_RESPONSE" | python3 -m json.tool

# 提取第一个任务ID
TASK_ID=$(echo "$TASKS_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo ""
echo "🎯 找到任务ID: $TASK_ID"
echo ""

# 步骤3: 完成任务
if [ -n "$TASK_ID" ]; then
    echo "✅ 步骤3: 完成任务"
    echo "=================================="
    
    COMPLETE_REQUEST='{
        "variables": {
            "approved": true,
            "comment": "API演示自动批准"
        },
        "comment": "演示完成任务"
    }'
    
    echo "发送请求: POST $BASE_URL/tasks/$TASK_ID/complete"
    COMPLETE_RESPONSE=$(curl -s -X POST "$BASE_URL/tasks/$TASK_ID/complete" \
        -H "Content-Type: application/json" \
        -d "$COMPLETE_REQUEST")
    
    echo "响应结果:"
    echo "$COMPLETE_RESPONSE" | python3 -m json.tool
    echo ""
    
    # 验证任务状态
    echo "🔍 步骤4: 验证任务完成后的状态"
    echo "=================================="
    
    echo "发送请求: GET $BASE_URL/tasks"
    NEW_TASKS_RESPONSE=$(curl -s "$BASE_URL/tasks")
    
    echo "当前待办任务:"
    echo "$NEW_TASKS_RESPONSE" | python3 -m json.tool
    echo ""
    
else
    echo "❌ 未找到可完成的任务"
fi

# 步骤5: 查看流程实例状态
echo "📊 步骤5: 查看流程实例状态"
echo "=================================="

echo "发送请求: GET $BASE_URL/instances"
INSTANCES_RESPONSE=$(curl -s "$BASE_URL/instances")

echo "流程实例列表:"
echo "$INSTANCES_RESPONSE" | python3 -m json.tool
echo ""

# 总结
echo "🎉 演示完成！"
echo "=================================="
echo "📚 您已经学会了如何："
echo "1. ✅ 启动请假流程: POST $BASE_URL/instances/leave/start"
echo "2. ✅ 查看待办任务: GET $BASE_URL/tasks"
echo "3. ✅ 完成任务: POST $BASE_URL/tasks/{taskId}/complete"
echo ""
echo "🌐 其他有用的链接:"
echo "- Camunda管理界面: http://localhost:8080/camunda (admin/admin)"
echo "- H2数据库控制台: http://localhost:8080/h2-console"
echo "- API测试文档: ./API_TESTING.md"
echo "- 完整部署文档: ./DEPLOYMENT.md"
echo "- 故障排除指南: ./TROUBLESHOOTING.md"
echo ""
echo "💡 提示: 您可以重复运行此脚本来测试更多流程"
echo "=================================="