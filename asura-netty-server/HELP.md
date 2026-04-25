# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/#build-image)

### 建立长连接

1. Client启动，连接Server成功时，发送连接标识，说明是建立长连接
2. ServerHandler中读取Client消息，如果是长连接，就不关闭通道

### 关闭长连接

1. Client使用长连接发送消息，说明要关闭长连接，并等待连接关闭
2. ServerHandler中读取Client消息，如果是关闭长连接，就给该通道添加关闭监听

### 建立短连接

1. 通过工具服务连接Server成功时，发送连接标识，说明是建立短连接
2. ServerHandler中读取Client消息，如果是短连接，就给该通道添加关闭监听
