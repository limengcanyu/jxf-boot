#!/bin/bash

# 工作流流程部署脚本
# 自动部署所有BPMN流程定义

echo "=========================================="
echo "工作流流程自动部署"
echo "=========================================="

# 设置认证信息
AUTH="admin:admin123"
BASE_URL="http://localhost:8080"
DEPLOY_API="$BASE_URL/api/workflow/deploy"

# 等待应用启动完成
wait_for_app() {
    echo "等待应用启动完成..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -u $AUTH "$BASE_URL/actuator/health" > /dev/null 2>&1; then
            echo "✓ 应用启动完成"
            return 0
        fi
        echo "等待中... ($attempt/$max_attempts)"
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "✗ 应用启动超时"
    return 1
}

# 部署单个流程
deploy_process() {
    local process_file=$1
    echo "部署流程: $process_file"
    
    local response=$(curl -s -u $AUTH -X POST "$DEPLOY_API?processName=$process_file" 2>/dev/null)
    local status_code=$(curl -s -o /dev/null -u $AUTH -w "%{http_code}" -X POST "$DEPLOY_API?processName=$process_file" 2>/dev/null)
    
    if [ "$status_code" = "200" ]; then
        echo "✓ $process_file 部署成功"
        echo "  响应: $response"
    else
        echo "✗ $process_file 部署失败 (HTTP $status_code)"
        echo "  响应: $response"
        return 1
    fi
}

# 检查流程是否已部署
check_deployed_processes() {
    echo "检查已部署的流程..."
    local response=$(curl -s -u $AUTH "$BASE_URL/api/workflow/definitions" 2>/dev/null)
    echo "当前已部署的流程:"
    echo "$response" | jq -r '.[] | "- \(.key) (\(.name)) - 版本 \(.version)"' 2>/dev/null || echo "$response"
}

# 主流程
main() {
    # 等待应用启动
    if ! wait_for_app; then
        echo "应用未启动，无法部署流程"
        exit 1
    fi
    
    echo ""
    echo "开始部署流程定义..."
    echo ""
    
    # 定义需要部署的流程文件
    local processes=(
        "leave-request.bpmn20.xml"
        "purchase-request.bpmn20.xml"
    )
    
    local success_count=0
    local total_count=${#processes[@]}
    
    # 部署每个流程
    for process in "${processes[@]}"; do
        if deploy_process "$process"; then
            success_count=$((success_count + 1))
        fi
        echo ""
    done
    
    echo "=========================================="
    echo "部署结果统计:"
    echo "- 总计: $total_count 个流程"
    echo "- 成功: $success_count 个流程"
    echo "- 失败: $((total_count - success_count)) 个流程"
    echo "=========================================="
    
    # 显示最终部署状态
    echo ""
    check_deployed_processes
    
    if [ $success_count -eq $total_count ]; then
        echo ""
        echo "🎉 所有流程部署完成！"
        return 0
    else
        echo ""
        echo "❌ 部分流程部署失败，请检查日志"
        return 1
    fi
}

# 执行主流程
main