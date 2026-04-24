package com.spring.boot.tomcat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermission {

    /**
     * 是否必须
     *
     * @return
     */
    boolean required() default false;

    /**
     * 角色
     *
     * @return
     */
    String[] roles();
}
