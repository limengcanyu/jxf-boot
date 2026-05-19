# spring-boot-actuator

## 端点地址 = /actuator 前缀 + 端点ID
## Shows application health information
http://localhost:8080/actuator/health

## Displays arbitrary application info
http://localhost:8080/actuator/info

## Shows and modifies the configuration of loggers in the application
http://localhost:8080/actuator/loggers

## Exposes properties from Spring’s ConfigurableEnvironment
http://localhost:8080/actuator/env

## Shows ‘metrics’ information for the current application
## 获取指标列表
http://localhost:8080/actuator/metrics
## 查看具体指标详情
http://localhost:8080/actuator/metrics/jvm.memory.max

## Displays a collated list of all @RequestMapping paths
http://localhost:8080/actuator/mappings

## Exposes audit events information for the current application
http://localhost:8080/actuator/auditevents

## Displays a complete list of all the Spring beans in your application
http://localhost:8080/actuator/beans

## Hypermedia for Actuator Web Endpoints
## 所有端点的发现页
http://localhost:8080/actuator

## web方式公开所有端点
management.endpoints.web.exposure.include=*

