package com.spring.boot.multi.datasource.transaction.aop;

import com.spring.boot.multi.datasource.transaction.annotation.MultiTransactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源事务切面
 *
 */
@Slf4j
@Aspect
@Component
public class MultiTransactionDynamicAop {

    @Resource
    private ApplicationContext applicationContext;

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
        log.debug("Thread ID: {} Name: {} 多数据源动态事务处理 开始......", Thread.currentThread().getId(), Thread.currentThread().getName());

        Deque<Map<PlatformTransactionManager, TransactionStatus>> transactionManagers = new ArrayDeque<>();

        String[] transactionManagerList = multiTransactional.transactionManager();

        log.debug("Thread ID: {} Name: {} 多数据源动态事务处理 开始......", Thread.currentThread().getId(), Thread.currentThread().getName());
        beginTransaction(transactionManagers, transactionManagerList);

        Object retVal;
        try {
            // start stopwatch
            retVal = pjp.proceed();
        } catch (Exception ex) {
            log.debug("Thread ID: {} Name: {} 多数据源动态事务处理 回滚......", Thread.currentThread().getId(), Thread.currentThread().getName());
            rollbackTransaction(transactionManagers);
            throw ex;
        }

        log.debug("Thread ID: {} Name: {} 多数据源动态事务处理 提交......", Thread.currentThread().getId(), Thread.currentThread().getName());
        commitTransaction(transactionManagers);

        log.debug("Thread ID: {} Name: {} 多数据源动态事务处理 结束......", Thread.currentThread().getId(), Thread.currentThread().getName());
        // stop stopwatch
        return retVal;
    }

    void beginTransaction(Deque<Map<PlatformTransactionManager, TransactionStatus>> transactionManagers, String[] transactionManagerList) {
        for (String s : transactionManagerList) {
            PlatformTransactionManager transactionManager = applicationContext.getBean(s, PlatformTransactionManager.class);
            TransactionStatus transactionStatus = transactionManager.getTransaction(def);
            log.debug("begin 事务管理器: " + transactionManager + " 事务状态: " + transactionStatus);
            Map<PlatformTransactionManager, TransactionStatus> map = new HashMap<>();
            map.put(transactionManager, transactionStatus);
            transactionManagers.push(map);
        }
    }

    void rollbackTransaction(Deque<Map<PlatformTransactionManager, TransactionStatus>> transactionManagers) {
        while (!ObjectUtils.isEmpty(transactionManagers.peek())) {
            Map<PlatformTransactionManager, TransactionStatus> map = transactionManagers.pop();
            PlatformTransactionManager transactionManager = (PlatformTransactionManager) map.keySet().toArray()[0];
            TransactionStatus transactionStatus = map.get(transactionManager);
            log.debug("rollback 事务管理器: " + transactionManager + " 事务状态: " + transactionStatus);
            transactionManager.rollback(transactionStatus);
        }
    }

    void commitTransaction(Deque<Map<PlatformTransactionManager, TransactionStatus>> transactionManagers) {
        while (!ObjectUtils.isEmpty(transactionManagers.peek())) {
            Map<PlatformTransactionManager, TransactionStatus> map = transactionManagers.pop();
            PlatformTransactionManager transactionManager = (PlatformTransactionManager) map.keySet().toArray()[0];
            TransactionStatus transactionStatus = map.get(transactionManager);
            log.debug("commit 事务管理器: " + transactionManager + " 事务状态: " + transactionStatus);
            transactionManager.commit(transactionStatus);
        }
    }
}
