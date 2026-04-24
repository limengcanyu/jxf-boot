# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)

### 参考

https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin/#building-images

### 项目命令

```shell
# 使用jdk17
# powershell添加/更新环境变量
$env:JAVA_HOME="E:\Java\temurin-17.0.5"
$env:path=-join("E:\Java\temurin-17.0.5\bin;", "$env:path")

# 项目打包并构建镜像
mvn clean package k8s:build k8s:resource

# 生成k8s资源
mvn k8s:resource

# 复制镜像到上传目录
rm -Force E:\迅雷下载\docker-build.tar
cp target\docker\jkube\spring-boot-jkube\latest\tmp\docker-build.tar E:\迅雷下载

rm -Force E:\迅雷下载\docker-build.tar
cp target\docker\rock-boot\spring-boot-jkube\0.0.1-SNAPSHOT\tmp\docker-build.tar E:\迅雷下载

# 从文件加载镜像
docker load --input docker-build.tar
docker image ls

# 查看镜像信息
docker inspect spring-boot-jkube:0.0.1-SNAPSHOT

# 查看镜像信息 指定项内容
docker inspect -f {{".Size"}} rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 查看镜像历史
docker history rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 查看镜像历史 具体信息
docker history --no-trunc rock-boot/spring-boot-jib:0.0.1-SNAPSHOT

# 运行容器
docker run -d --name spring-boot-jkube -p 8080:8080 jkube/spring-boot-jkube:latest
docker ps

docker run -d --name spring-boot-jkube -p 8080:8080 rock-boot/spring-boot-jkube:0.0.1-SNAPSHOT
docker ps

# 查看容器日志
docker logs spring-boot-jkube

# 调用接口
curl http://localhost:8080/hello

# 清理容器和镜像
clear
docker rm -f spring-boot-jkube
docker rmi spring-boot-jkube:0.0.1-SNAPSHOT
docker rmi rock-boot/spring-boot-jkube:0.0.1-SNAPSHOT
docker rmi rock-boot/spring-boot-jkube:latest
docker rmi jkube/spring-boot-jkube:latest
docker ps -a && docker images

```