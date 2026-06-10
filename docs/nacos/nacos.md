# Nacos

1. 修改配置文件

nacos\conf\application.properties

```properties
nacos.core.auth.plugin.nacos.token.secret.key=MIICWwIBAAKBgQDAXlJ4sLz0mGkE5XlQ8zJkLz0mGkE5XlQ8zJkLz0mGkE5XlQ8zJkLz

nacos.core.auth.server.identity.key=nacos
nacos.core.auth.server.identity.value=nacos

```

2. 启动

```cmd
cd E:\dev-tools\nacos-server-3.2.2\nacos\bin

# 启动服务器
startup.cmd -m standalone

# 关闭服务器
shutdown.cmd

```

3. Nacos控制台页面

http://127.0.0.1:8080
