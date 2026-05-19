FROM openjdk:17.0.2-oracle as builder
WORKDIR .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# skywalking: 在 apache/skywalking-java-agent 基础上构建镜像
FROM apache/skywalking-java-agent:8.10.0-java17
ENV JAVA_OPTS="-Xms1024m -Xmx1024m"
WORKDIR .
COPY --from=builder ./dependencies/ ./
COPY --from=builder ./spring-boot-loader/ ./
COPY --from=builder ./snapshot-dependencies/ ./
COPY --from=builder ./application/ ./
# arthas: 复制镜像 hengyunabc/arthas:3.6.1 中 /opt/arthas 目录到当前镜像 /opt/arthas 目录
COPY --from=hengyunabc/arthas:3.6.1 /opt/arthas /opt/arthas
# skywalking: 在 apache/skywalking-java-agent 镜像中已有 SW_AGENT_NAME SW_AGENT_COLLECTOR_BACKEND_SERVICES 环境变量
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -DSW_AGENT_NAME=$SW_AGENT_NAME -DSW_AGENT_COLLECTOR_BACKEND_SERVICES=$SW_AGENT_COLLECTOR_BACKEND_SERVICES org.springframework.boot.loader.JarLauncher"]
