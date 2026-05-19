# spring-boot-custom-starter

### Creating Your Own Starter

一个完整的 Spring Boot starter 包含两个组件：

1. autoconfigure module 包含 auto-configuration code.
2. starter module 提供对 autoconfigure module 的依赖，以及一些其它依赖. starter 应该提供使用该 starter 时所需的一切.

注：可以将 autoconfigure module 和 starter module 合而为一。

### Naming

1. starter module 的名字不要以 spring-boot 开头。
2. 在 starter 之后命名一个组合 module。例如，假设你要创建一个名称为 acme 的 starter，那么 auto-configure module 的名字可以命名为 acme-spring-boot-autoconfigure，
   starter 可以命名为 acme-spring-boot-starter 。如果只需要一个组合两者的 module ，那么可以命名为 acme-spring-boot-starter 。

### Configuration keys

1. 配置key不要以 server, management, spring 等开头。可以以自己的名字开头，例如 acme 。
2. 确保为每个属性都添加 field javadoc，使配置key文档化。
3. 对 @ConfigurationProperties field Javadoc 只使用纯文本，因为它们在被添加到JSON之前是不会被处理的。

确保描述一致的规则：

1. Do not start the description by "The" or "A".
   描述不要以 "The" or "A" 开头。
2. For boolean types, start the description with "Whether" or "Enable".
   对于 boolean 类型，描述以 "Whether" or "Enable" 开头。
3. For collection-based types, start the description with "Comma-separated list"
   对于基于集合的类型，描述以 "Comma-separated list" 开头。
4. Use java.time.Duration rather than long and describe the default unit if it differs from milliseconds, e.g. "If a duration suffix is not specified, seconds will be used".
   如果默认单元不是微秒的话，使用 java.time.Duration 描述，不要使用 long，例如，"If a duration suffix is not specified, seconds will be used".
5. Do not provide the default value in the description unless it has to be determined at runtime.
   描述中不要提供默认值，除非该值必须在运行时决定。

### autoconfigure Module

### Starter Module

