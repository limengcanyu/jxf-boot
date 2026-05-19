#!/bin/bash

echo "=================================="
echo "Camunda工作流核心API测试脚本"
echo "=================================="

BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api/workflow"

echo "测试API基础路径: $API_BASE"
echo "请确保应用已启动在 $BASE_URL"
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
test_api() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}测试: $description${NC}"
    echo "请求: $method $url"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$url")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST "$url" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    # 分离响应体和状态码
    body=$(echo "$response" | head -n -1)
    status_code=$(echo "$response" | tail -n 1)
    
    if [ "$status_code" -ge 200 ] && [ "$status_code" -lt 300 ]; then
        echo -e "${GREEN}✓ 成功 (HTTP $status_code)${NC}"
        echo "响应: $body"
    else
        echo -e "${RED}✗ 失败 (HTTP $status_code)${NC}"
        echo "响应: $body"
    fi
    
    echo ""
    return $status_code
}

# 全局变量存储流程实例ID和任务ID
PROCESS_INSTANCE_ID=""
TASK_ID=""

echo "=================================="
echo "1. 启动请假流程测试"
echo "=================================="

# 启动请假流程: POST /api/workflow/start
LEAVE_REQUEST='{
    "applicant": "张三",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "家庭聚会需要请假",
    "phone": "13800138000",
    "urgency": 2
}'

echo "测试数据:"
echo "$LEAVE_REQUEST" | python3 -m json.tool 2>/dev/null || echo "$LEAVE_REQUEST"
echo ""

# 注意：实际的API路径是 /instances/leave/start，让我们测试这个
test_api "POST" "$API_BASE/instances/leave/start" "$LEAVE_REQUEST" "启动请假流程"

# 也测试通用的启动流程API
START_PROCESS_REQUEST='{
    "processDefinitionKey": "leave-process",
    "businessKey": "leave-001",
    "variables": {
        "applicant": "李四",
        "leaveType": "病假",
        "days": 2,
        "reason": "感冒需要休息",
        "approved": false
    }
}'

test_api "POST" "$API_BASE/instances/start" "$START_PROCESS_REQUEST" "通用启动流程"

echo "=================================="
echo "2. 查看待办任务测试"
echo "=================================="

# 查看待办任务: GET /api/workflow/tasks
test_api "GET" "$API_BASE/tasks" "" "获取所有待办任务"

# 获取特定分配人的任务
test_api "GET" "$API_BASE/tasks/assignee/manager" "" "获取经理的待办任务"

# 获取特定分配人的任务
test_api "GET" "$API_BASE/tasks/assignee/director" "" "获取总监的待办任务"

echo "=================================="
echo "3. 获取任务ID用于完成任务测试"
echo "=================================="

# 获取第一个任务的ID
echo "正在获取任务列表以获取任务ID..."
TASKS_RESPONSE=$(curl -s "$API_BASE/tasks")
echo "任务列表响应: $TASKS_RESPONSE"

# 尝试从响应中提取任务ID（简单的JSON解析）
TASK_ID=$(echo "$TASKS_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ -n "$TASK_ID" ]; then
    echo -e "${GREEN}找到任务ID: $TASK_ID${NC}"
    echo ""
    
    echo "=================================="
    echo "4. 完成任务测试"
    echo "=================================="
    
    # 完成任务: POST /api/workflow/complete-task/{taskId}
    # 注意：实际API路径是 /tasks/{taskId}/complete
    COMPLETE_TASK_REQUEST='{
        "variables": {
            "approved": true,
            "comment": "批准请假申请",
            "approver": "manager"
        },
        "comment": "经理审批通过"
    }'
    
    echo "完成任务数据:"
    echo "$COMPLETE_TASK_REQUEST" | python3 -m json.tool 2>/dev/null || echo "$COMPLETE_TASK_REQUEST"
    echo ""
    
    test_api "POST" "$API_BASE/tasks/$TASK_ID/complete" "$COMPLETE_TASK_REQUEST" "完成任务 (任务ID: $TASK_ID)"
    
    echo "=================================="
    echo "5. 验证任务完成后的状态"
    echo "=================================="
    
    # 再次获取任务列表，验证任务是否已完成
    test_api "GET" "$API_BASE/tasks" "" "验证任务完成后的待办任务列表"
    
    # 获取流程实例状态
    test_api "GET" "$API_BASE/instances" "" "查看流程实例状态"
    
else
    echo -e "${RED}未找到可用的任务ID，跳过完成任务测试${NC}"
    echo "这可能是因为没有活动的流程实例或任务"
    echo ""
fi

echo "=================================="
echo "6. 其他测试"
echo "=================================="

# 获取流程定义
test_api "GET" "$API_BASE/definitions" "" "获取流程定义"

# 获取流程实例
test_api "GET" "$API_BASE/instances" "" "获取流程实例"

# 获取历史任务
test_api "GET" "$API_BASE/history/tasks" "" "获取历史任务"

echo "=================================="
echo "测试总结"
echo "=================================="
echo "测试完成时间: $(date)"
echo ""
echo "主要测试的API:"
echo "1. POST $API_BASE/instances/leave/start - 启动请假流程"
echo "2. GET  $API_BASE/tasks - 查看待办任务"  
echo "3. POST $API_BASE/tasks/{taskId}/complete - 完成任务"
echo ""
echo "其他有用的API:"
echo "- GET  $API_BASE/definitions - 获取流程定义"
echo "- GET  $API_BASE/instances - 获取流程实例"
echo "- GET  $API_BASE/tasks/assignee/{assignee} - 获取特定用户的任务"
echo "- GET  $API_BASE/history/tasks - 获取历史任务"
echo ""
echo "Camunda管理界面:"
echo "- 访问 $BASE_URL/camunda 进入管理界面"
echo "- 默认用户名/密码: demo/demo"
echo ""
echo "=================================="