package com.spring.boot.mybatisplus.druid.multi.datasource.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 数据源切换AOP</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/24 0024
 */
@Component
@Aspect
@Order(-100) //这是为了保证AOP在事务注解之前生效,Order的值越小,优先级越高
public class DataSourceSwitchAspect {

    @Pointcut("execution(* com.spring.boot.mybatisplus.druid.multi.datasource.artanis.dao.mapper.*.*(..))")
    private void dbArtanisAspect() {
    }

    @Pointcut("execution(* com.spring.boot.mybatisplus.druid.multi.datasource.samuro.dao.mapper.*.*(..))")
    private void dbSamuroAspect() {
    }

    @Pointcut("execution(* com.spring.boot.mybatisplus.druid.multi.datasource.valeera.dao.mapper.*.*(..))")
    private void dbValeeraAspect() {
    }

    /**
     * 在执行Mapper中的方法之前切换到指定数据源
     */
    @Before( "dbArtanisAspect()" )
    public void dbArtanis() {
        System.out.println("切换到数据源 阿塔尼斯");
        DBContextHolder.setDbType(DBTypeEnum.dbArtanis);
    }

    @Before( "dbSamuroAspect()" )
    public void dbSamuro() {
        System.out.println("切换到数据源 萨穆罗");
        DBContextHolder.setDbType(DBTypeEnum.dbSamuro);
    }

    @Before("dbValeeraAspect()" )
    public void dbValeera () {
        System.out.println("切换到数据源 瓦莉拉");
        DBContextHolder.setDbType(DBTypeEnum.dbValeera);
    }
}
