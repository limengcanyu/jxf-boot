# Getting Started

### 项目结构

```d
asura-sse/
├── src/main/java/org/asura/sse/
│   ├── config/
│   │   └── SseConfig.java          # CORS配置
│   ├── controller/
│   │   └── SseController.java      # REST API控制器
│   ├── exception/
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   ├── model/
│   │   ├── SseEvent.java           # SSE事件模型
│   │   ├── SseMessageRequest.java  # 消息请求模型
│   │   └── SseSession.java         # SSE会话模型
│   ├── repository/
│   │   └── SseSessionRepository.java    # 会话存储仓库
│   ├── service/
│   │   └── SseService.java         # 核心SSE服务
│   └── AsuraSseApplication.java    # 应用入口
├── src/main/resources/
│   ├── static/
│   │   └── index.html              # 可视化演示页面
│   └── application.yaml            # 应用配置
└── src/test/java/org/asura/sse/
    └── SseServiceTest.java         # 单元测试
```

### 核心功能

功能	说明
SSE连接管理	支持客户端ID和频道订阅
频道订阅/取消订阅	支持按频道广播消息
广播消息	向所有连接或指定频道发送消息
定向消息	支持向特定客户端或会话发送消息
连接统计	实时监控活跃连接数
优雅断开	自动清理超时/断开的连接

### API 接口

方法	路径	功能
GET	/sse/connect	建立SSE连接
POST	/sse/subscribe	订阅频道
POST	/sse/unsubscribe	取消订阅频道
POST	/sse/broadcast	广播消息
POST	/sse/send/{clientId}	向指定客户端发送消息
POST	/sse/send/session/{sessionId}	向指定会话发送消息
GET	/sse/stats	获取连接统计

### 可视化演示

启动应用后，访问 http://localhost:8080 即可看到完整的 SSE 演示页面，支持：

* 实时连接状态显示
* 多频道订阅管理
* 消息发送与接收
* 服务统计监控

服务已在 http://localhost:8080 运行，可以直接访问体验！

