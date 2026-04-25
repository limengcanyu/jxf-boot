# 🧪 API测试文档

## 📋 测试概览

本项目提供了完整的Camunda工作流API测试套件，包括自动化测试脚本和手动测试指南。

## 🚀 自动化测试

### 完整功能演示
```bash
./execute-workflow-demo.sh
```
**测试内容**:
- ✅ 应用状态检查
- ✅ 启动请假流程
- ✅ 查看待办任务
- ✅ 完成任务操作
- ✅ 验证流程状态
- ✅ 查看流程实例

### 简化API测试
```bash
./simple-test-api.sh
```
**测试内容**:
- ✅ 基础API连通性
- ✅ 流程启动功能
- ✅ 任务查询功能
- ✅ 任务完成功能

### 基础连通性测试
```bash
curl http://localhost:8080/api/workflow/definitions
```

## 📝 手动测试指南

### 1. 启动请假流程

#### 基础请假申请
```bash
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{
    "applicant": "张三",
    "leaveType": "年假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-27",
    "days": 3,
    "reason": "家庭聚会",
    "phone": "13800138000",
    "urgency": 2
  }'
```

#### 短期请假申请（≤3天）
```bash
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{
    "applicant": "李四",
    "leaveType": "病假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-25",
    "days": 1,
    "reason": "感冒就医",
    "phone": "13800138001",
    "urgency": 3
  }'
```

#### 长期请假申请（>3天）
```bash
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{
    "applicant": "王五",
    "leaveType": "事假",
    "startDate": "2024-08-25",
    "endDate": "2024-08-30",
    "days": 5,
    "reason": "家庭事务处理",
    "phone": "13800138002",
    "urgency": 1
  }'
```

**期望响应格式**:
```json
{
  "code": 200,
  "message": "请假流程启动成功",
  "data": {
    "id": "流程实例ID",
    "processDefinitionKey": "leave-process",
    "processDefinitionName": "请假流程",
    "businessKey": "业务标识",
    "suspended": false,
    "ended": false
  },
  "timestamp": 1234567890123
}
```

### 2. 查看待办任务

#### 获取所有任务
```bash
curl http://localhost:8080/api/workflow/tasks
```

#### 获取特定用户的任务
```bash
# 获取申请人的任务
curl http://localhost:8080/api/workflow/tasks/assignee/张三

# 获取经理的任务
curl http://localhost:8080/api/workflow/tasks/assignee/manager

# 获取总监的任务
curl http://localhost:8080/api/workflow/tasks/assignee/director
```

#### 根据流程实例查询任务
```bash
curl http://localhost:8080/api/workflow/tasks/process/{processInstanceId}
```

**期望响应格式**:
```json
{
  "code": 200,
  "message": "获取任务成功",
  "data": [
    {
      "id": "任务ID",
      "name": "任务名称",
      "description": "任务描述",
      "assignee": "分配人",
      "processInstanceId": "流程实例ID",
      "createTime": "创建时间",
      "dueDate": "到期时间"
    }
  ]
}
```

### 3. 完成任务

#### 完成提交申请任务
```bash
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "submitted": true,
      "submissionTime": "2024-08-24 10:00:00"
    },
    "comment": "申请已提交"
  }'
```

#### 经理审批通过
```bash
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "approved": true,
      "approver": "manager",
      "comment": "批准请假申请"
    },
    "comment": "经理审批通过"
  }'
```

#### 经理审批拒绝
```bash
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "approved": false,
      "approver": "manager",
      "rejectReason": "工作安排冲突",
      "comment": "拒绝请假申请"
    },
    "comment": "经理审批拒绝"
  }'
```

#### 总监审批通过
```bash
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "approved": true,
      "approver": "director",
      "comment": "总监批准长期请假"
    },
    "comment": "总监审批通过"
  }'
```

### 4. 查询操作

#### 获取流程定义
```bash
curl http://localhost:8080/api/workflow/definitions
```

#### 获取流程实例
```bash
# 获取所有流程实例
curl http://localhost:8080/api/workflow/instances

# 根据ID获取流程实例
curl http://localhost:8080/api/workflow/instances/{instanceId}
```

#### 获取历史记录
```bash
# 获取历史任务
curl http://localhost:8080/api/workflow/history/tasks

# 获取历史流程实例
curl http://localhost:8080/api/workflow/history/instances

# 根据流程定义获取历史实例
curl http://localhost:8080/api/workflow/history/instances/leave-process
```

## 🔄 完整流程测试场景

### 场景1: 短期请假（≤3天）自动批准流程
```bash
# 1. 启动短期请假流程
RESPONSE=$(curl -s -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{"applicant":"测试用户1","leaveType":"病假","days":2,"reason":"感冒"}')

# 2. 获取任务ID
TASK_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

# 3. 完成提交申请任务
curl -X POST http://localhost:8080/api/workflow/tasks/$TASK_ID/complete \
  -H "Content-Type: application/json" \
  -d '{"variables":{"submitted":true},"comment":"申请已提交"}'

# 4. 获取经理审批任务
MANAGER_TASKS=$(curl -s http://localhost:8080/api/workflow/tasks/assignee/manager)
echo $MANAGER_TASKS

# 5. 完成经理审批（批准）
MANAGER_TASK_ID=$(echo $MANAGER_TASKS | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
curl -X POST http://localhost:8080/api/workflow/tasks/$MANAGER_TASK_ID/complete \
  -H "Content-Type: application/json" \
  -d '{"variables":{"approved":true},"comment":"经理批准"}'

# 6. 验证流程完成
curl http://localhost:8080/api/workflow/instances
```

### 场景2: 长期请假（>3天）需要总监审批
```bash
# 1. 启动长期请假流程
curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
  -H "Content-Type: application/json" \
  -d '{"applicant":"测试用户2","leaveType":"年假","days":7,"reason":"年假旅游"}'

# 2. 按流程完成各个任务...
# （类似场景1，但需要经过总监审批环节）
```

### 场景3: 请假被拒绝流程
```bash
# 测试审批拒绝的情况
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{"variables":{"approved":false,"rejectReason":"工作繁忙"},"comment":"拒绝申请"}'
```

## 📊 性能测试

### 并发测试
```bash
# 使用Apache Bench进行并发测试
ab -n 100 -c 10 -T application/json -p request.json http://localhost:8080/api/workflow/instances/leave/start

# request.json内容:
# {"applicant":"性能测试","leaveType":"测试","days":1,"reason":"性能测试"}
```

### 批量数据测试
```bash
# 创建批量测试脚本
for i in {1..50}; do
  curl -X POST http://localhost:8080/api/workflow/instances/leave/start \
    -H "Content-Type: application/json" \
    -d "{\"applicant\":\"测试用户$i\",\"leaveType\":\"测试\",\"days\":1,\"reason\":\"批量测试$i\"}"
  sleep 0.1
done
```

## 🔍 测试验证要点

### 成功标准
- ✅ HTTP状态码为200
- ✅ 响应包含正确的JSON格式
- ✅ 流程实例成功创建
- ✅ 任务正确分配
- ✅ 流程状态正确流转

### 数据验证
```bash
# 验证流程实例数量
curl -s http://localhost:8080/api/workflow/instances | grep -o '"id"' | wc -l

# 验证任务数量
curl -s http://localhost:8080/api/workflow/tasks | grep -o '"id"' | wc -l

# 验证流程定义
curl -s http://localhost:8080/api/workflow/definitions | grep "leave-process"
```

## 🐛 常见测试问题

### 1. 任务ID获取失败
```bash
# 确保正确解析JSON响应
TASKS=$(curl -s http://localhost:8080/api/workflow/tasks)
echo $TASKS | python3 -m json.tool
```

### 2. 任务完成失败
```bash
# 检查任务是否存在且未完成
curl http://localhost:8080/api/workflow/tasks/{taskId}

# 确认请求格式正确
curl -X POST http://localhost:8080/api/workflow/tasks/{taskId}/complete \
  -H "Content-Type: application/json" \
  -d '{"variables":{},"comment":"测试"}'
```

### 3. 流程状态异常
```bash
# 查看流程实例详情
curl http://localhost:8080/api/workflow/instances/{instanceId}

# 查看历史记录
curl http://localhost:8080/api/workflow/history/tasks/process/{instanceId}
```

## 📈 测试报告

运行测试后，可以通过以下方式查看结果：

### 基础统计
```bash
echo "流程定义数量: $(curl -s http://localhost:8080/api/workflow/definitions | grep -o '"id"' | wc -l)"
echo "活跃流程实例: $(curl -s http://localhost:8080/api/workflow/instances | grep -o '"id"' | wc -l)"
echo "待办任务数量: $(curl -s http://localhost:8080/api/workflow/tasks | grep -o '"id"' | wc -l)"
```

### 详细分析
访问Camunda管理界面 http://localhost:8080/camunda 查看：
- 流程执行统计
- 任务处理时间
- 系统性能指标

这份文档提供了完整的API测试指南，从基础测试到复杂场景，确保工作流系统的各个功能都能正常工作。