# spring-boot-async

``````
启动后浏览器访问：
http://localhost:8080/doSyncTask
http://localhost:8080/doWithoutReturnTask
http://localhost:8080/doWithoutReturnTask2
http://localhost:8080/doReturnFutureResultTask
http://localhost:8080/doReturnFutureResultTask2
http://localhost:8080/doReturnListenableFutureResultTask
http://localhost:8080/doReturnListenableFutureResultTask2
http://localhost:8080/doReturnCompletableFutureResultTask
http://localhost:8080/doReturnCompletableFutureResultTask2
http://localhost:8080/doWebAsyncTask
``````

1. 启用异步支持注解 @EnableAsync
2. 定义Bean，类中的方法使用 @Async 注解，Spring 会为该 Bean 使用 advice ，从而进行异步支持
注： 在同一个类中调用 @Async 注解的方法时异步支持无效，原因是无法为自身类创建代理，所以必须在另一个类中定义异步方法，然后使用该类
``````

spring默认任务池配置位置 spring.boot.autoconfigure 包中 TaskExecutionAutoConfiguration 类
