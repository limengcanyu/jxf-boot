# spring-boot-shardingsphere-jdbc

注:
entity 中的事件字段不能用 LocalDateTime，会报异常 java.sql.SQLFeatureNotSupportedException: getObject with type
使用 Date 则没有问题，例如使用 Date createTime; 不能用 LocalDateTime createTime;

报异常是因为构造 LocalDateTime 指定的时间不规范，不能用 00:00:00

