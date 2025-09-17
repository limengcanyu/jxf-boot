以下是一些 Spring Cloud 微服务架构中，生产实用且有大厂运用场景的单点登录方案 ：
1. OAuth 2.0/OpenID Connect（OIDC）：OAuth 2.0 聚焦授权，OIDC 是构建于它之上的身份认证层 。Google、GitHub、微软 Azure AD，以及国内的企业微信、钉钉、飞书等大厂平台，都支持 OAuth 2.0 或 OIDC 。Spring Cloud 可通过 Spring Security 的 OAuth2 Client 模块来进行集成，开发便捷 。该方案标准化程度高、安全性强，生态系统丰富，适合企业级多系统整合，以及对接微信等第三方登录的场景 。
2. Keycloak 集成方案：Keycloak 是开源的企业级单点登录与身份管理系统，能与 Spring Cloud 无缝衔接。它提供用户管理、角色权限、协议适配等众多功能。许多大型集团企业和技术型公司，在构建复杂的内部微服务群单点登录时，会选用 Keycloak 。利用它可以快速搭建起符合行业规范的统一身份认证体系，降低自主研发身份系统的复杂度与工作量。
3. 网关 + JWT+Redis+Spring Security 模式 ：Spring Cloud Gateway 作 API 网关，拦截全部请求。用户登录成功后，Auth 服务利用 Spring Security 校验信息，校验通过后签发 JWT 。JWT 携带用户信息在各微服务间流转，Redis 用于保存 JWT 状态，比如记录有效期、维护注销黑名单等 。这种组合在互联网电商、互联网金融等大厂的微服务工程中较为普及，它能有效应对高并发，并且让各个微服务的认证逻辑解耦，便于扩展与维护 。
4. CAS 方案 ：CAS 即中央认证服务，是一个老牌的单点登录框架 。它的核心机制是用户访问子系统时，若发现没登录，会被重定向到 CAS 服务器登录，登录成功后，CAS 发放票据（Ticket） ，子系统拿着票据验证，验证通过就确认用户登录状态 。一些传统大厂、大型国企，尤其是政府、教育等领域的大厂关联系统，因为系统改造幅度小、对标准协议依赖弱等原因，会采用 CAS 作为单点登录方案 。


在 IntelliJ IDEA 中使用其内置的 HTTP 客户端来测试需要 JWT Token 的 API 接口非常方便。

步骤如下：

创建 HTTP 请求文件：
在你的项目 src 目录同级（或任何你喜欢的地方，比如新建一个 http 目录）创建一个新文件，例如命名为 api-test.http。IDEA 会自动识别 .http 扩展名并提供语法高亮和执行功能。
编写 HTTP 请求：
在 api-test.http 文件中，你可以写多个请求块，每个块用 ### 分隔。
首先，你需要一个请求来获取 JWT Token（假设你有一个登录接口，比如 /api/auth/login）。我们将把获取到的 Token 存储在一个变量中，供后续请求使用。
然后，编写访问 /api/test/user 和 /api/test/admin 的 GET 请求，并在 Authorization 头中使用存储的 Token。
示例 api-test.http 文件内容：

```d
### 1. 用户登录并获取 JWT Token
# 将 {your-base-url} 替换为你的实际后端地址，例如 http://localhost:8080
# 将 {your-username} 和 {your-password} 替换为有效的用户凭据

POST {{base_url}}/api/auth/login
Content-Type: application/json

{
  "username": "{{username}}",
  "password": "{{password}}"
}

> {%
    // 脚本：从登录响应中提取 JWT Token 并存储到 client.global.variables 中
    // 假设你的 JWT Token 在响应体的 "token" 字段中
    client.global.set("auth_token", response.body.token);
    // 如果 Token 在响应头中，例如 Authorization: Bearer <token>
    // client.global.set("auth_token", response.headers.valueOf("Authorization").substring(7)); // 去掉 "Bearer " 前缀
    client.log("Token stored in variable 'auth_token': " + client.global.get("auth_token"));
%}

### 2. 测试 /api/test/user 接口 (需要 ROLE_USER 或以上)
# 使用存储的 Token

GET {{base_url}}/api/test/user
Authorization: Bearer {{auth_token}}

### 3. 测试 /api/test/admin 接口 (需要 ROLE_ADMIN)
# 使用存储的 Token

GET {{base_url}}/api/test/admin
Authorization: Bearer {{auth_token}}


### 4. (可选) 使用不同用户测试 /api/test/admin (预期失败)
# 你可以为不同用户设置不同的环境变量，或者在这里直接使用另一个用户的凭据重新登录获取 Token

# POST {{base_url}}/api/auth/login
# Content-Type: application/json
#
# {
#   "username": "{{non_admin_username}}",
#   "password": "{{non_admin_password}}"
# }
#
# > {%
#     client.global.set("auth_token_non_admin", response.body.token);
# %}
#
# GET {{base_url}}/api/test/admin
# Authorization: Bearer {{auth_token_non_admin}}

```

IDEA HTTP Client Test for JWT Secured APIs
**3.  **

为了方便管理不同的环境（如开发、测试、生产）和用户凭据，建议使用 IDEA 的 HTTP Client 环境变量 功能。
在 IDEA 中，按 Ctrl+Alt+S (Windows/Linux) 或 Cmd+, (Mac) 打开设置。
导航到 Tools -> HTTP Client -> Environments。
添加一个新环境，例如 dev。
在该环境中定义变量：

```d
{
    "dev": {
        "base_url": "http://localhost:8080",
        "username": "your_test_username", // 替换为你的测试用户名
        "password": "your_test_password", // 替换为你的测试密码
        "non_admin_username": "regular_user", // (可选) 用于测试权限不足的用户
        "non_admin_password": "user_password"  // (可选)
    }
}
```

在 .http 文件中，使用双花括号 {{variable_name}} 来引用这些变量。
在 IDEA 的 HTTP Client 工具栏中，选择你定义的环境（例如 dev）。

**4.  **

打开 api-test.http 文件。
将光标放在第一个请求块（登录请求）内。
按 Alt+Enter 或点击请求行左侧出现的绿色箭头/播放按钮来执行该请求。
查看响应，确认是否成功获取了 Token，并且脚本已将其存储。
接着，将光标移动到 /api/test/user 的请求块内，同样方式执行。检查响应内容和状态码（应为 200 OK）。
最后，执行 /api/test/admin 的请求。如果当前用户有 ROLE_ADMIN，应该成功；如果没有，则应返回 403 Forbidden。
通过这种方式，你可以方便地在 IDEA 内部测试你的 JWT 保护的 API 接口。

# 如果我要在另一个服务中获取当前登录用户的信息，怎么做

当你在另一个服务（我们称之为 Service B）中需要获取当前登录用户的信息时，关键在于从 Spring Security 的 SecurityContext 中获取。

因为你在之前的配置中，已经通过 AuthTokenFilter 在请求处理链的早期阶段解析了 JWT Token，并将解析出的用户信息（封装在 Authentication 对象中）存入了 SecurityContextHolder，所以在同一个请求线程中，任何地方都可以通过 SecurityContextHolder 获取到这个信息。

核心方法：

使用 SecurityContextHolder.getContext().getAuthentication() 来获取当前认证信息。

示例代码：

假设你在 Service B 中有一个服务类 UserService，需要获取当前登录用户的用户名。

```java
package com.example.serviceb.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    /**
     * 获取当前登录用户的用户名。
     * @return Optional 包含用户名，如果未认证则为空。
     */
    public Optional<String> getCurrentUsername() {
        // 1. 从 SecurityContext 获取 Authentication 对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 检查是否已认证
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // 3. 获取 Principal (通常是我们 UserDetails 的实现)
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // 4. 如果是 UserDetails，获取用户名
                return Optional.of(((UserDetails) principal).getUsername());
            } else if (principal instanceof String) {
                // 5. 如果 Principal 是 String (例如直接存的用户名)
                return Optional.of((String) principal);
            }
        }

        // 6. 如果未认证或无法获取用户名，返回空 Optional
        return Optional.empty();
    }

    /**
     * 获取当前登录用户的完整 UserDetails 信息。
     * @return Optional 包含 UserDetails，如果未认证则为空。
     */
    public Optional<UserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return Optional.of((UserDetails) principal);
            }
        }

        return Optional.empty();
    }

    // --- 示例业务方法 ---
    public String getUserProfileInfo() {
        Optional<String> usernameOpt = getCurrentUsername();
        if (usernameOpt.isPresent()) {
            String username = usernameOpt.get();
            // 这里可以调用 DAO/Repository 查询用户详细信息
            // User user = userRepository.findByUsername(username);
            return "Profile info for user: " + username;
        } else {
            // 处理未认证情况，或者抛出异常
            return "User not authenticated";
            // throw new AccessDeniedException("User not authenticated");
        }
    }
}
```

在 Controller 中使用:

```java
package com.example.serviceb.controller;

import com.example.serviceb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile() {
        // UserService 内部会通过 SecurityContextHolder 获取当前用户信息
        String profileInfo = userService.getUserProfileInfo();
        return ResponseEntity.ok(profileInfo);
    }
}
```

重要前提：

* SecurityContextHolder 策略: 默认情况下，Spring Security 使用 ThreadLocal 策略 (MODE_THREADLOCAL) 来存储 SecurityContext。这意味着 SecurityContextHolder.getContext() 获取到的上下文是与当前处理请求的线程绑定的。只要你在同一个请求处理线程中调用 UserService 的方法，就能获取到正确的认证信息。
* JWT Filter 已配置: 确保你的 Service B 项目也像之前示例一样，配置了 AuthTokenFilter 并将其添加到 Spring Security 的过滤器链中。这样，每个进入 Service B 的请求都会先经过 JWT 验证，并将用户信息放入 SecurityContext。
* 依赖: 确保你的 pom.xml 或 build.gradle 包含了 Spring Security 依赖。

总结:

通过在 Service B 中引入 Spring Security，并配置好 JWT 认证过滤器后，你就可以在任何需要的地方（如 Service 类、Controller 类）通过 SecurityContextHolder.getContext().getAuthentication() 轻松获取到当前登录用户的信息。这是微服务架构下，基于 JWT 实现用户上下文传递的标准做法之一。
