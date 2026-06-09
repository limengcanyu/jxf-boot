# Getting Started

https://skywalking.apache.org/

### 启动 SkyWalking

##### Windows启动

```shell
# 设置JDK
set JAVA_HOME=E:\jdk\OpenJDK17U-jdk_x64_windows_hotspot_17.0.17_10\jdk-17.0.17+10
echo %JAVA_HOME%
set PATH=%JAVA_HOME%\bin;%PATH%
echo %PATH%

# JDK 17 启动 SkyWalking
E:

cd E:\dev-tools\apache-skywalking-apm-10.1.0\apache-skywalking-apm-bin

bin\startup.bat

```

UI 地址：

http://127.0.0.1:8080

### IDEA VM参数

```shell
-javaagent:E:/dev-tools/apache-skywalking-java-agent-9.6.0/skywalking-agent/skywalking-agent.jar
-Dskywalking.collector.backend_service=127.0.0.1:11800
-Dskywalking.agent.service_name=asura-skywalking

```

### Java命令启动参数

```shell
java -javaagent:E:/Skywalking/apache-skywalking-java-agent-8.13.0/skywalking-agent/skywalking-agent.jar=agent.service_name=spring-boot-skywalking,collector.backend_service=127.0.0.1:11800 -jar xx.jar

```
