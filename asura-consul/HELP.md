在 Consul 里创建配置（UI 操作）

打开 UI → Key/Value → 创建：

```d
Key：config/demo-service/data
```

Value 内容：

```yaml
app:
  name: 测试服务
  env: test
```

访问接口查看效果：

http://localhost:8080/config

修改 Consul 配置后，接口会自动刷新，无需重启服务！
