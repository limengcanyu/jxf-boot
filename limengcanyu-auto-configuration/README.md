# spring-boot-auto-configuration

Spring Boot会检查jar包中META-INF/spring.factories文件是否存在。该文件应该列出来所有的配置类。

自动配置必需以该方式加载。确保这些配置类定义在特殊的包中，并且不能被组件扫描覆盖。进一步说，自动配置类不应该让组件扫描来查找其它组件。特殊情况下应该使用@Import代替。



