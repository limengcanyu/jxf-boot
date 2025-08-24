#!/bin/bash

echo "=================================="
echo "Camunda工作流API简化测试"
echo "=================================="

BASE_URL="http://localhost:8080/api/workflow"

echo "测试 1: 启动请假流程"
echo "=============================="
echo "POST $BASE_URL/instances/leave/start"

LEAVE_REQUEST='{
    "applicant": "测试用户",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "测试请假流程",
    "phone": "13800138000",
    "urgency": 2
}'

echo "请求数据: $LEAVE_REQUEST"
echo ""
echo "响应结果:"
curl -s -X POST "$BASE_URL/instances/leave/start" \
    -H "Content-Type: application/json" \
    -d "$LEAVE_REQUEST" | python3 -m json.tool

echo ""
echo ""

echo "测试 2: 查看待办任务"
echo "=============================="
echo "GET $BASE_URL/tasks"
echo ""
echo "响应结果:"
curl -s "$BASE_URL/tasks" | python3 -m json.tool

echo ""
echo ""

echo "测试 3: 获取经理的待办任务"
echo "=============================="
echo "GET $BASE_URL/tasks/assignee/manager"
echo ""
echo "响应结果:"
curl -s "$BASE_URL/tasks/assignee/manager" | python3 -m json.tool

echo ""
echo ""

echo "测试 4: 获取第一个任务ID并完成任务"
echo "=============================="

# 获取任务ID
TASK_RESPONSE=$(curl -s "$BASE_URL/tasks")
echo "所有任务: $TASK_RESPONSE"
echo ""

# 提取第一个任务ID（使用更简单的方法）
TASK_ID=$(echo "$TASK_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ -n "$TASK_ID" ]; then
    echo "找到任务ID: $TASK_ID"
    echo ""
    
    COMPLETE_TASK_REQUEST='{
        "variables": {
            "approved": true,
            "comment": "批准请假申请"
        },
        "comment": "测试完成任务"
    }'
    
    echo "POST $BASE_URL/tasks/$TASK_ID/complete"
    echo "请求数据: $COMPLETE_TASK_REQUEST"
    echo ""
    echo "响应结果:"
    curl -s -X POST "$BASE_URL/tasks/$TASK_ID/complete" \
        -H "Content-Type: application/json" \
        -d "$COMPLETE_TASK_REQUEST" | python3 -m json.tool
    
    echo ""
    echo ""
    
    echo "测试 5: 验证任务完成后的状态"
    echo "=============================="
    echo "GET $BASE_URL/tasks"
    echo ""
    echo "响应结果:"
    curl -s "$BASE_URL/tasks" | python3 -m json.tool
    
else
    echo "未找到任务ID，跳过完成任务测试"
fi

echo ""
echo ""

echo "测试 6: 获取流程定义"
echo "=============================="
echo "GET $BASE_URL/definitions"
echo ""
echo "响应结果:"
curl -s "$BASE_URL/definitions" | python3 -m json.tool

echo ""
echo ""

echo "测试 7: 获取流程实例"
echo "=============================="
echo "GET $BASE_URL/instances"
echo ""
echo "响应结果:"
curl -s "$BASE_URL/instances" | python3 -m json.tool

echo ""
echo ""

echo "=================================="
echo "测试完成！"
echo "=================================="
echo "您可以访问以下地址查看更多信息："
echo "- Camunda管理界面: http://localhost:8080/camunda"
echo "- H2数据库控制台: http://localhost:8080/h2-console"
echo "=================================="