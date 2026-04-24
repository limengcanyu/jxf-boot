# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)

### jib配置参考

https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#outputpaths-object

### 项目命令

```shell
# 使用jdk17
# powershell添加/更新环境变量
$env:JAVA_HOME="E:\Java\temurin-17.0.5"
$env:path=-join("E:\Java\temurin-17.0.5\bin;", "$env:path")

# 编译项目
mvn clean compile

rm -Force E:\迅雷下载\spring-boot-jib.tar
cp E:\IdeaProjects\rock-boot\spring-boot-jib\target\spring-boot-jib.tar E:\迅雷下载

# 从文件加载镜像
docker load --input spring-boot-jib.tar
docker image ls | grep rock-boot

# 查看镜像信息
docker inspect rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 查看镜像信息 指定项内容
docker inspect -f {{".Size"}} rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 查看镜像历史
docker history rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 查看镜像历史 具体信息
docker history --no-trunc rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 运行容器
docker run -d --name spring-boot-jib -p 8080:8080 rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 调用接口
curl http://localhost:8080/hello

# 清理容器和镜像
docker rm -f spring-boot-jib
docker rmi rock-boot/spring-boot-jib:0.0.1-SNAPSHOT
docker ps -a && docker images

```