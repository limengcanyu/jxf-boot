https://docs.spring-boot-admin.com/3.0.0/getting-started.html

https://docs.spring-boot-admin.com/4.0.4/docs/index

# Spring Boot Admin 3 服务端和客户端实现指南

## 一、服务端实现

### 1. 依赖配置
在服务端项目的 `pom.xml` 中添加以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-server</artifactId>
    </dependency>
</dependencies>
```

### 2. 主应用类配置
在主应用类上添加 `@EnableAdminServer` 注解：

```java
package org.akuma.admin.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class AkumaAdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AkumaAdminServerApplication.class, args);
    }
}
```

### 3. 配置文件
创建或修改 `application.yml` 文件：

```yaml
server:
  port: 8080  # 服务端端口

spring:
  application:
    name: spring-boot-admin-server
```

## 二、客户端实现

### 1. 依赖配置
在客户端项目的 `pom.xml` 中添加以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

### 2. 配置文件
创建或修改 `application.yml` 文件：

```yaml
spring:
  application:
    name: spring-boot-admin-client
  boot:
    admin:
      client:
        url: http://localhost:8080  # 服务端地址

management:
  endpoints:
    web:
      exposure:
        include: "*"  # 暴露所有端点
  endpoint:
    health:
      show-details: always  # 显示健康详情
  info:
    env:
      enabled: true  # 启用环境信息
```

### 3. 安全配置
创建安全配置类，允许所有请求：

```java
package org.akuma.admin.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityPermitAllConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
                .csrf().disable().build();
    }
}
```

## 三、运行和验证

1. **启动服务端**：运行 `AkumaAdminServerApplication`
2. **启动客户端**：运行 `AkumaAdminClientApplication`
3. **访问服务端界面**：打开浏览器访问 `http://localhost:8080`
4. **验证客户端注册**：在服务端界面上应该能看到客户端实例已注册

## 四、高级配置

### 1. 服务端安全配置
如果需要为服务端添加安全认证：

```yaml
spring:
  security:
    user:
      name: admin
      password: admin123
```

### 2. 客户端安全配置
如果服务端有安全认证，客户端需要配置凭据：

```yaml
spring:
  boot:
    admin:
      client:
        url: http://localhost:8080
        username: admin
        password: admin123
```

### 3. 自定义监控信息
在客户端添加自定义监控信息：

```java
@Component
public class CustomInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("custom", 
            Collections.singletonMap("version", "1.0.0"));
    }
}
```

## 五、注意事项

1. **版本兼容性**：Spring Boot Admin 3 需要 Spring Boot 3.x 版本
2. **端口冲突**：确保服务端和客户端使用不同的端口
3. **网络可达性**：确保客户端能够访问服务端的 URL
4. **端点暴露**：客户端需要正确配置端点暴露，否则服务端无法获取监控信息
5. **安全配置**：在生产环境中，应该使用更严格的安全配置，而不是简单的 permitAll

通过以上配置，您就可以搭建一个完整的 Spring Boot Admin 3 监控系统，实现对 Spring Boot 应用的实时监控和管理。
