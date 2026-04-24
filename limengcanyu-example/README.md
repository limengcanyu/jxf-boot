## 构建镜像

docker build -t jiangxingfeng/spring-boot-example:0.0.1-SNAPSHOT .

docker images | grep jiangxingfeng

## 运行容器

docker run -d --name spring-boot-example -p 8081:8080 jiangxingfeng/spring-boot-example:0.0.1-SNAPSHOT

docker run -d --name spring-boot-example -p 8081:8080 \
    -e SW_AGENT_NAME=spring-boot-example -e SW_AGENT_COLLECTOR_BACKEND_SERVICES=192.168.136.135:11800 \
    -e SW_GRPC_LOG_SERVER_HOST=192.168.136.135,SW_GRPC_LOG_SERVER_PORT=11800 \    
    jiangxingfeng/spring-boot-example:0.0.1-SNAPSHOT

192.168.136.135:11800: 主机IP，localhost为容器本地IP
-e SW_AGENT_NAME=spring-boot-example -e SW_AGENT_COLLECTOR_BACKEND_SERVICES=192.168.136.135:11800: 服务接入SkyWalking
-e SW_GRPC_LOG_SERVER_HOST=192.168.136.135,SW_GRPC_LOG_SERVER_PORT=11800: 日志接入SkyWalking

docker logs spring-boot-example

docker logs --since="2016-07-01" --tail=10 spring-boot-example

docker logs -ft --tail=100 spring-boot-example

http://192.168.136.128:8080/hello

## 在容器中启动arthas

docker exec -it spring-boot-example /bin/bash

java -jar arthas-boot.jar

## 清理

docker stop spring-boot-example && docker rm spring-boot-example

docker rmi jiangxingfeng/spring-boot-example:0.0.1-SNAPSHOT

docker images | grep jiangxingfeng
