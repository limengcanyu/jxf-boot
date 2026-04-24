package com.spring.boot.multi.datasource.transaction.annotation;


import java.lang.annotation.*;

/**
 * 多数据源事务注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MultiTransactional {

    String[] transactionManager() default {};

}
