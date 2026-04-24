package com.spring.boot.multi.datasource.transaction.aop;

import com.spring.boot.multi.datasource.transaction.annotation.MultiTransactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 多数据源事务切面
 */
@Slf4j
//@Aspect
//@Component
public class MultiTransactionStaticAop {

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private MongoTransactionManager mongoTransactionManager;

    /**
     * 事务声明
     */
    private final DefaultTransactionDefinition def = new DefaultTransactionDefinition();

    static {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // explicitly setting the transaction name is something that can be done only programmatically
//        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
    }

    @Pointcut("@annotation(com.spring.boot.multi.datasource.transaction.annotation.MultiTransactional)")
    public void pointcut() {
    }

    @Around("@annotation(multiTransactional)")
    public Object doBasicProfiling(ProceedingJoinPoint pjp, MultiTransactional multiTransactional) throws Throwable {
        log.debug("多数据源事务处理 开始......");

        TransactionStatus mysqlStatus = dataSourceTransactionManager.getTransaction(def);
        TransactionStatus mongoStatus = mongoTransactionManager.getTransaction(def);

        Object retVal;
        try {
            // start stopwatch
            retVal = pjp.proceed();
        } catch (Exception ex) {
            mongoTransactionManager.rollback(mongoStatus);
            dataSourceTransactionManager.rollback(mysqlStatus);
            throw ex;
        }

        mongoTransactionManager.commit(mongoStatus);
        dataSourceTransactionManager.commit(mysqlStatus);

        log.debug("多数据源事务处理 结束......");
        // stop stopwatch
        return retVal;
    }
}
