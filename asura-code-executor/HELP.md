完整接口调用示例（可直接 Postman 测试）
1. 请求头

```
X-API-Key: dev_java_runner_2026  # 开发环境
Content-Type: multipart/form-data
```

2. 上传.java 文件接口

* 地址：POST http://localhost:8080/api/java/run/upload
* 参数：form-data，key=file，选择.java文件

3. 文本代码接口

* 地址：POST http://localhost:8080/api/java/run/code
* 参数：
*    javaCode: Java 源码字符串
*    className(可选): 类名，不填自动解析

4. 测试用合法代码

```java
public class Test {
    public static void main(String[] args) {
        System.out.println("生产系统测试成功");
        System.out.println("1+2=" + (1+2));
    }
}

```

1. 基础输出测试（已验证）

```java
public class Test { 
    public static void main(String[] args) { 
        System.out.println("执行成功！"); 
        System.out.println("100 + 200 = " + (100 + 200)); 
    } 
}
```

✅ 预期：output 显示完整打印内容，runSuccess=true。

2. 输出大小限制测试（防 OOM）

```java
public class Test { 
    public static void main(String[] args) { 
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("测试输出内容");
        }
        System.out.println(sb);
    } 
}
```

✅ 预期：输出被截断，message 提示「输出内容过大，已截断」，无 OOM。

3. 超时控制测试（防死循环）

```java
public class Test { 
    public static void main(String[] args) throws InterruptedException { 
        while (true) {
            Thread.sleep(100);
        }
    } 
}
```

✅ 预期：8 秒后超时终止，message 提示「执行超时，已强制终止」。

4. 危险代码拦截测试

```java
public class Test { 
    public static void main(String[] args) { 
        Runtime.getRuntime().exec("ls"); // 危险系统命令
    } 
}
```

✅ 预期：执行前被拦截，message 提示「禁止调用危险方法：Runtime.exec」。

生产环境上线前最后检查清单

1. 安全配置：

* 修改 application-prod.yml 中的 api-key 为强随机字符串（如 prod_${random.value}）；
* 配置 ip-whitelist 为实际允许访问的 IP 段（禁止 0.0.0.0/0）；

2. 资源限制：

* 确认 max-output-kb（输出大小）、timeout（执行超时）、max-memory-mb（内存限制）配置符合业务需求；

3. 监控日志：

* 确保日志目录挂载到宿主机，审计日志包含「IP、类名、执行结果」；
* 接入 Prometheus 监控（编译 / 执行成功率、超时次数、安全拦截次数）；

4. 容器部署：

* 使用 docker-compose.yml 部署，限制容器 CPU / 内存（如 cpus: '1'、memory: 512M）；
* Nginx 层配置 IP 白名单 + 限流，双重防护。


关键验证项（确保多线程安全）

1. 输出流隔离验证
   每个线程的 output 仅包含自身的打印内容，无其他线程的输出；
   测试完成后，应用日志（如控制台）仍能正常输出，无输出流丢失。
2. 资源限制验证
   大输出任务被精准截断，无 OOM；
   超时任务在 8 秒后被终止，无无限阻塞；
   危险代码被提前拦截，无安全风险。
3. 线程安全验证
   无 NullPointerException/ClassCastException 等并发异常；
   所有线程执行完成后，ThreadLocal 中的输出流被清空，无内存泄漏。
