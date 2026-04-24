# spring-boot-actuator

## 端点地址 = /actuator 前缀 + 端点ID
## Shows application health information
localhost:8080/actuator/health

## Displays arbitrary application info
localhost:8080/actuator/info

## Shows and modifies the configuration of loggers in the application
localhost:8080/actuator/loggers

## Exposes properties from Spring’s ConfigurableEnvironment
localhost:8080/actuator/env

## Shows ‘metrics’ information for the current application
## 获取指标列表
localhost:8080/actuator/metrics
## 查看具体指标详情
localhost:8080/actuator/metrics/jvm.memory.max

## Displays a collated list of all @RequestMapping paths
localhost:8080/actuator/mappings

## Exposes audit events information for the current application
localhost:8080/actuator/auditevents

## Displays a complete list of all the Spring beans in your application
localhost:8080/actuator/beans

## Hypermedia for Actuator Web Endpoints
## 所有端点的发现页
localhost:8080/actuator

## web方式公开所有端点
management.endpoints.web.exposure.include=*

