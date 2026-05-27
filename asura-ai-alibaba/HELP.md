
# Asura AI Alibaba - 测试脚本

## 服务启动

```bash
# 进入项目目录
cd asura-ai-alibaba

# 编译项目
mvn clean compile -q

# 启动服务
mvn spring-boot:run
```

## 环境变量配置

在启动前需要设置阿里云 DashScope API Key：

```bash
# Linux/Mac
export AI_DASHSCOPE_API_KEY=your-api-key

# Windows (PowerShell)
$env:AI_DASHSCOPE_API_KEY="your-api-key"
```

## 测试脚本

### 1. 健康检查

```bash
curl -s http://localhost:8080/api/health
```

**预期响应**:
```json
{"status":"UP","service":"asura-ai-alibaba","version":"1.0.0-SNAPSHOT"}
```

### 2. 聊天接口 (POST)

```bash
curl -s -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello, who are you?","model":"qwen-max"}'
```

**预期响应**:
```json
{"content":"Hello! I'm Qwen...","model":"qwen-max"}
```

### 3. 聊天接口 (GET - 流式)

```bash
curl -s "http://localhost:8080/api/ai/chat/stream?message=What is AI?"
```

### 4. 对话记忆 (Conversation Memory)

**第一步：创建对话并告诉AI信息**

```bash
curl -s -X POST http://localhost:8080/api/ai/chat/memory/my-conversation \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello, my name is John"}'
```

**第二步：继续对话（AI会记住之前的内容）**

```bash
curl -s -X POST http://localhost:8080/api/ai/chat/memory/my-conversation \
  -H "Content-Type: application/json" \
  -d '{"message":"What is my name?"}'
```

**预期响应**：AI应该回答 "Your name is John."

**清除对话记忆**

```bash
curl -s -X DELETE http://localhost:8080/api/ai/chat/memory/my-conversation
```

**GET方式带记忆聊天**

```bash
curl -s "http://localhost:8080/api/ai/chat/memory/my-conversation?message=Tell me more"
```

### 5. 图片生成

```bash
curl -s -X POST http://localhost:8080/api/ai/image/generate \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "a beautiful sunset over the ocean",
    "style": "写实风格",
    "resolution": "1024x1024",
    "n": 1
  }'
```

### 5. 语音合成 (TTS)

```bash
curl -s -X POST http://localhost:8080/api/ai/speech/tts \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello from Asura AI Alibaba",
    "voice": "Aiyue",
    "rate": 1.0,
    "volume": 1.0,
    "format": "mp3"
  }'
```

### 6. 语音识别 (STT)

```bash
curl -s -X POST http://localhost:8080/api/ai/speech/stt \
  -H "Content-Type: application/json" \
  -d '{
    "audioBase64": "your-audio-base64-string",
    "format": "mp3",
    "enablePunctuation": true
  }'
```

### 7. 向量嵌入

```bash
curl -s -X POST http://localhost:8080/api/ai/embedding \
  -H "Content-Type: application/json" \
  -d '{
    "texts": ["Hello world", "AI is amazing", "Spring Boot is awesome"],
    "model": "text-embedding-v1"
  }'
```

### 8. 向量存储 (Redis)

**存储向量到 Redis**

```bash
curl -s -X POST http://localhost:8080/api/ai/embedding/store \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Artificial intelligence is the simulation of human intelligence processes by machines",
      "Machine learning is a subset of AI that uses algorithms to learn from data",
      "Deep learning uses neural networks with multiple layers to learn from data",
      "Natural language processing enables computers to understand human language"
    ],
    "namespace": "test"
  }'
```

**预期响应**:
```json
{"message":"Embeddings stored successfully","count":"4","status":"success","namespace":"test"}
```

**搜索相似文档**

```bash
curl -s "http://localhost:8080/api/ai/embedding/search?query=machine%20learning&topK=3"
```

**预期响应**:
```json
{
  "count": 3,
  "query": "machine learning",
  "results": [
    "Artificial intelligence is the simulation of human intelligence processes by machines",
    "Machine learning is a subset of AI that uses algorithms to learn from data",
    "Deep learning uses neural networks with multiple layers to learn from data"
  ],
  "topK": 3
}
```

### 9. AI Agent 智能代理

**获取 Agent 信息**

```bash
curl -s http://localhost:8080/api/ai/agent/info
```

**预期响应**:
```json
{
  "name": "Asura AI Agent",
  "version": "1.0.0",
  "description": "基于 Spring AI 的智能代理服务",
  "capabilities": ["对话交互", "工具调用", "对话记忆", "多模态支持"],
  "tools": [
    {"name": "weather", "description": "获取天气信息"},
    {"name": "calculator", "description": "数学计算"},
    {"name": "vector_search", "description": "向量数据库搜索"}
  ]
}
```

**Agent 对话**

```bash
curl -s -X POST http://localhost:8080/api/ai/agent/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello, who are you?","conversationId":"my-conversation"}'
```

**Agent 工具调用**

```bash
# 计算器工具
curl -s -X POST http://localhost:8080/api/ai/agent/chat/tools \
  -H "Content-Type: application/json" \
  -d '{"message":"calculate 2 + 3 * 4"}'

# 天气工具（使用英文避免编码问题）
curl -s -X POST http://localhost:8080/api/ai/agent/chat/tools \
  -H "Content-Type: application/json" \
  -d '{"message":"weather in Beijing"}'

# 向量搜索工具
curl -s -X POST http://localhost:8080/api/ai/agent/chat/tools \
  -H "Content-Type: application/json" \
  -d '{"message":"search machine learning"}'
```

**PowerShell 测试（推荐，支持中文）**

```powershell
# 计算器工具
Invoke-RestMethod -Uri 'http://localhost:8080/api/ai/agent/chat/tools' `
  -Method Post -ContentType 'application/json' `
  -Body '{"message":"计算 2 + 3 * 4"}' | ConvertTo-Json

# 天气工具
Invoke-RestMethod -Uri 'http://localhost:8080/api/ai/agent/chat/tools' `
  -Method Post -ContentType 'application/json' `
  -Body '{"message":"北京天气"}' | ConvertTo-Json

# 向量搜索工具
Invoke-RestMethod -Uri 'http://localhost:8080/api/ai/agent/chat/tools' `
  -Method Post -ContentType 'application/json' `
  -Body '{"message":"搜索机器学习资料"}' | ConvertTo-Json
```

## 批量测试脚本

创建 `test-all.sh` 文件：

```bash
#!/bin/bash

echo "=== Asura AI Alibaba 功能测试 ==="
echo ""

echo "1. 健康检查"
curl -s http://localhost:8080/api/health | head -c 100
echo ""
echo ""

echo "2. 聊天测试"
curl -s -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"What is Spring Boot?"}' | head -c 200
echo ""
echo ""

echo "3. 图片生成"
curl -s -X POST http://localhost:8080/api/ai/image/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt":"cat"}' | head -c 100
echo ""
echo ""

echo "4. 语音合成"
curl -s -X POST http://localhost:8080/api/ai/speech/tts \
  -H "Content-Type: application/json" \
  -d '{"text":"Test"}' | head -c 100
echo ""
echo ""

echo "5. 向量嵌入"
curl -s -X POST http://localhost:8080/api/ai/embedding \
  -H "Content-Type: application/json" \
  -d '{"texts":["Hello"]}' | head -c 100
echo ""
echo ""

echo "=== 测试完成 ==="
```

运行测试：

```bash
chmod +x test-all.sh
./test-all.sh
```

## 注意事项

1. **API Key**: 确保已正确配置 `AI_DASHSCOPE_API_KEY` 环境变量
2. **服务状态**: 测试前确保服务已启动在端口 8080
3. **网络连接**: 需要网络连接以访问阿里云 DashScope 服务
4. **权限**: 确保 API Key 有足够的权限调用相关服务

## API 文档

| 接口 | 方法 | 功能 |
|------|------|------|
| `/api/health` | GET | 健康检查 |
| `/api/ai/chat` | POST | 聊天对话 |
| `/api/ai/chat/stream` | GET | 流式聊天 |
| `/api/ai/image/generate` | POST | 图片生成 |
| `/api/ai/speech/tts` | POST | 语音合成 |
| `/api/ai/speech/stt` | POST | 语音识别 |
| `/api/ai/embedding` | POST | 向量嵌入 |
| `/api/ai/embedding/store` | POST | 存储向量到 Redis |
| `/api/ai/embedding/search` | GET | 搜索相似文档 |
| `/api/ai/agent/chat` | POST | Agent 对话 |
| `/api/ai/agent/chat/tools` | POST | Agent 工具调用 |
| `/api/ai/agent/info` | GET | Agent 信息 |