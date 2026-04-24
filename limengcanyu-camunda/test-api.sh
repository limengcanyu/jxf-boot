#!/bin/bash

echo "=========================="
echo "Camunda工作流API测试脚本"
echo "=========================="

BASE_URL="http://localhost:8080"

echo "测试前请确保应用已启动在 $BASE_URL"
echo ""

# 测试应用健康状态
echo "1. 测试应用健康状态..."
curl -s "$BASE_URL/actuator/health" | echo "Health check: $(cat)"
echo ""

# 获取所有流程定义
echo "2. 获取所有流程定义..."
curl -s "$BASE_URL/workflow/definitions" | echo "Process definitions: $(cat)"
echo ""

# 启动请假流程
echo "3. 启动请假流程..."
LEAVE_REQUEST='{
    "applicant": "张三",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "家庭聚会",
    "phone": "13800138000",
    "urgency": 2
}'

PROCESS_RESULT=$(curl -s -X POST "$BASE_URL/workflow/instances/leave/start" \
    -H "Content-Type: application/json" \
    -d "$LEAVE_REQUEST")

echo "Start process result: $PROCESS_RESULT"
echo ""

# 获取所有任务
echo "4. 获取所有任务..."
curl -s "$BASE_URL/workflow/tasks" | echo "All tasks: $(cat)"
echo ""

# 获取经理的任务
echo "5. 获取经理的任务..."
curl -s "$BASE_URL/workflow/tasks/assignee/manager" | echo "Manager tasks: $(cat)"
echo ""

# 获取所有流程实例
echo "6. 获取所有流程实例..."
curl -s "$BASE_URL/workflow/instances" | echo "Process instances: $(cat)"
echo ""

echo "=========================="
echo "测试完成"
echo "=========================="
echo ""
echo "手动测试步骤:"
echo "1. 访问 $BASE_URL/app 进入Camunda管理界面（admin/admin）"
echo "2. 在Tasklist中查看和处理待办任务"
echo "3. 在Cockpit中监控流程执行状态"
echo "4. 使用上述API接口进行流程操作"