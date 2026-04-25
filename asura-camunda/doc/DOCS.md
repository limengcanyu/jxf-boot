# 📚 项目文档索引

欢迎使用Spring Boot + Camunda工作流项目！以下是完整的文档指南：

## 🚀 快速开始

| 文档 | 描述 | 适用场景 |
|------|------|----------|
| [README.md](../README.md) | 一分钟快速启动指南 | 首次使用、快速验证 |
| [execute-workflow-demo.sh](../script/execute-workflow-demo.sh) | 完整功能演示脚本 | 学习工作流、功能验证 |
| [simple-test-api.sh](../script/simple-test-api.sh) | 简化API测试脚本 | 基础测试、日常验证 |

## 📖 详细文档

### 🔧 部署相关
| 文档 | 描述 | 适用场景 |
|------|------|----------|
| [DEPLOYMENT.md](DEPLOYMENT.md) | 完整部署指南 | 生产部署、详细配置 |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | 故障排除快速参考 | 问题解决、应急处理 |

### 🧪 测试相关
| 文档 | 描述 | 适用场景 |
|------|------|----------|
| [API_TESTING.md](API_TESTING.md) | API测试完整指南 | 功能测试、性能测试 |

## 🎯 使用场景指南

### 👨‍💻 开发人员
1. **首次接触项目**: [README.md](../README.md) → [execute-workflow-demo.sh](../script/execute-workflow-demo.sh)
2. **深入了解**: [DEPLOYMENT.md](DEPLOYMENT.md) → [API_TESTING.md](API_TESTING.md)
3. **遇到问题**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### 🔧 运维人员
1. **部署应用**: [DEPLOYMENT.md](DEPLOYMENT.md)
2. **故障处理**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
3. **健康检查**: [simple-test-api.sh](../script/simple-test-api.sh)

### 🧪 测试人员
1. **功能测试**: [API_TESTING.md](API_TESTING.md)
2. **自动化测试**: [execute-workflow-demo.sh](../script/execute-workflow-demo.sh)
3. **问题诊断**: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### 📋 项目经理
1. **项目概览**: [README.md](../README.md)
2. **功能演示**: [execute-workflow-demo.sh](../script/execute-workflow-demo.sh)
3. **部署计划**: [DEPLOYMENT.md](DEPLOYMENT.md)

## 🔗 外部资源

### 技术文档
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Camunda 官方文档](https://docs.camunda.org/)
- [Maven 官方文档](https://maven.apache.org/guides/)

### 在线工具
- [Camunda Modeler](https://camunda.com/download/modeler/) - BPMN流程设计工具
- [Postman](https://www.postman.com/) - API测试工具
- [H2 Database](http://www.h2database.com/) - 内存数据库

## 📱 快速访问

### 应用地址（启动后访问）
- **API基础地址**: http://localhost:8080/api/workflow
- **管理界面**: http://localhost:8080/camunda
- **数据库控制台**: http://localhost:8080/h2-console

### 常用命令
```bash
# 启动应用
./mvnw spring-boot:run

# 运行完整测试
./execute-workflow-demo.sh

# 快速API测试
./simple-test-api.sh

# 检查应用状态
curl http://localhost:8080/api/workflow/definitions

# 解决端口占用
lsof -ti :8080 | xargs kill -9
```

## 📝 文档更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0.0 | 2024-08-24 | 初始文档集合创建 |
| | | - 部署指南 |
| | | - API测试文档 |
| | | - 故障排除指南 |
| | | - 快速开始指南 |

## 🤝 贡献指南

如需更新文档：
1. 保持文档的一致性和准确性
2. 更新相关的交叉引用
3. 在文档更新日志中记录变更
4. 验证所有示例代码和命令的正确性

## 📞 支持

如果文档中的信息不够详细或遇到未覆盖的问题：
1. 首先查看 [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
2. 检查项目日志文件
3. 参考官方技术文档
4. 联系技术支持团队

---

**🏆 祝您使用愉快！** 这个工作流系统将帮助您构建强大的业务流程管理应用。