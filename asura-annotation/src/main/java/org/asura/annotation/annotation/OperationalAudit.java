package org.asura.annotation.annotation;


import java.lang.annotation.*;

/**
 * 操作审计注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperationalAudit {
    /**
     * 方法名称
     *
     * @return
     */
    String name() default "";

    /**
     * 操作类型
     *
     * @return
     */
    int type();
}
