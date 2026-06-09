# Help

https://github.com/spring-ai-community/awesome-spring-ai

### 技术栈
asura-ai-agent项目
帮我实现一个企业级的知识库AI应用，技术栈包括智谱模型、记忆存储使用hsqldb，ORM使用mybatis-plus，向量存储使用Redis。使用RAG技术。
上传文档可以是PDF、Word、Excel等格式。

spring ai的版本是1.1.6，不要用其他版本

---

## 🧪 功能测试指南

### 启动应用
```bash
# 进入项目目录
cd asura-ai

# 设置环境变量（可选，用于RAG问答）
export ZHIPUAI_API_KEY=your-api-key

# 启动应用
mvn spring-boot:run
```

### 测试步骤

#### 1. 用户注册
```bash
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123", "email": "admin@example.com"}'
```

#### 2. 用户登录
```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```
**响应示例：**
```json
{
  "user": {"id": "...", "username": "admin", "email": "admin@example.com", "role": "USER"},
  "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

#### 3. 获取当前用户信息
```bash
curl -s -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <token>"
```

#### 4. 修改密码
```bash
curl -s -X PUT http://localhost:8080/api/auth/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"oldPassword": "admin123", "newPassword": "newpassword"}'
```

#### 5. 创建文档分类
```bash
curl -s -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name": "Technical Docs", "parentId": null, "description": "Technical documents"}'
```

#### 6. 获取分类列表
```bash
curl -s -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer <token>"
```

#### 7. 获取文档列表（分页）
```bash
curl -s -X GET "http://localhost:8080/api/documents?page=0&size=5" \
  -H "Authorization: Bearer <token>"
```

#### 8. 获取所有文档
```bash
curl -s -X GET http://localhost:8080/api/documents/all \
  -H "Authorization: Bearer <token>"
```

#### 9. 搜索文档
```bash
curl -s -X GET "http://localhost:8080/api/documents/search?keyword=AI" \
  -H "Authorization: Bearer <token>"
```

#### 10. RAG智能问答（需要配置AI API密钥）
```bash
curl -s -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"question": "What is machine learning?"}'
```

```bash
echo '{"question": "你的中文问题？"}' | curl -s -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json;charset=UTF-8" \
  -H "Authorization: Bearer <your-token>" \
  -d @-

echo '{"question": "AI"}' | curl -s -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json;charset=UTF-8" \
  -H "Authorization: Bearer asura-ai-test-token" \
  -d @-

echo '{"question": "产品设计"}' | curl -s -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json;charset=UTF-8" \
  -H "Authorization: Bearer asura-ai-test-token" \
  -d @-

echo '{"question": "人工智能"}' | curl -s -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json;charset=UTF-8" \
  -H "Authorization: Bearer asura-ai-test-token" \
  -d @-

```

#### 11. 退出登录
```bash
curl -s -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <token>"
```

#### 12. 验证退出后Token失效
```bash
curl -s -X GET "http://localhost:8080/api/documents/all" \
  -H "Authorization: Bearer <已注销的token>"
```

---

## 🔌 API接口汇总

| 模块 | 接口 | 方法 | 说明 | 是否需要认证 |
|------|------|------|------|-------------|
| 认证 | `/api/auth/register` | POST | 用户注册 | ❌ |
| 认证 | `/api/auth/login` | POST | 用户登录 | ❌ |
| 认证 | `/api/auth/me` | GET | 获取当前用户 | ✅ |
| 认证 | `/api/auth/change-password` | PUT | 修改密码 | ✅ |
| 认证 | `/api/auth/logout` | POST | 退出登录 | ✅ |
| 文档 | `/api/documents` | GET | 分页获取文档 | ✅ |
| 文档 | `/api/documents/all` | GET | 获取所有文档 | ✅ |
| 文档 | `/api/documents/search` | GET | 搜索文档 | ✅ |
| 文档 | `/api/documents/upload` | POST | 上传文档 | ✅ |
| 分类 | `/api/categories` | GET/POST | 获取/创建分类 | ✅ |
| RAG | `/api/rag/ask` | POST | 智能问答 | ✅ |

---

## ⚠️ 环境变量配置

```bash
# 智谱AI API Key（用于RAG问答）
export ZHIPUAI_API_KEY=your-api-key

# DeepSeek API Key（可选）
export DEEPSEEK_API_KEY=your-api-key
```