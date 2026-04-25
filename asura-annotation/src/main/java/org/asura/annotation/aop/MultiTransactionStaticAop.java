package org.asura.annotation.aop;

import org.asura.annotation.annotation.OperationalAudit;
import org.asura.annotation.constant.OperationalTypeConst;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 操作审计切面
 */
@Slf4j
@Aspect
@Component
public class MultiTransactionStaticAop {

    @Around("@annotation(operationalAudit)")
    public Object doBasicProfiling(ProceedingJoinPoint pjp, OperationalAudit operationalAudit) throws Throwable {
        String methodName = operationalAudit.name();
        String typeName = OperationalTypeConst.getOperationalTypeName(operationalAudit.type());

        log.debug("操作审计 方法：{} 类型：{} 开始......", methodName, typeName);
        long begin = System.currentTimeMillis();

        Object retVal;
        try {
            // start stopwatch
            retVal = pjp.proceed();
        } catch (Exception ex) {
            log.debug("操作审计 方法：{} 类型：{} 异常结束，耗时: {} 毫秒......", methodName, typeName, System.currentTimeMillis() - begin);
            costTimeLogging(methodName, typeName, begin, 1);
            throw ex;
        }

        log.debug("操作审计 方法：{} 类型：{} 正常结束，耗时: {} 毫秒......", methodName, typeName, System.currentTimeMillis() - begin);
        costTimeLogging(methodName, typeName, begin, 0);
        // stop stopwatch
        return retVal;
    }

    void costTimeLogging(String methodName, String typeName, long begin, int flag) {
        long cost = System.currentTimeMillis() - begin;

        if (cost > 3000) {
            if (flag == 1) { // 异常结束
                log.warn("操作审计 方法：{} 类型：{} 异常结束，耗时超过3秒......", methodName, typeName);
                return;
            }

            if (flag == 0) { // 正常结束
                log.warn("操作审计 方法：{} 类型：{} 正常结束，耗时超过3秒......", methodName, typeName);
                return;
            }
        }

        if (cost > 1000) {
            if (flag == 1) { // 异常结束
                log.warn("操作审计 方法：{} 类型：{} 异常结束，耗时超过1秒......", methodName, typeName);
                return;
            }

            if (flag == 0) { // 正常结束
                log.warn("操作审计 方法：{} 类型：{} 正常结束，耗时超过1秒......", methodName, typeName);
                return;
            }
        }
    }
}
