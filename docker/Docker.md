### Docker

docker build -f spring-boot-prometheus/Dockerfile -t jiangxingfeng/spring-boot-prometheus:0.0.1-SNAPSHOT ./spring-boot-prometheus

docker run --name spring-boot-prometheus -p 8080:8080 -d jiangxingfeng/spring-boot-prometheus:0.0.1-SNAPSHOT

docker stop spring-boot-prometheus

docker rm spring-boot-prometheus

docker rmi jiangxingfeng/spring-boot-prometheus:0.0.1-SNAPSHOT

### JVM

-Xms20m -Xmx20m


