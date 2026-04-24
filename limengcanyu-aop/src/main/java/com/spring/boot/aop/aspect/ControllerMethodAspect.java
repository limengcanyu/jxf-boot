package com.spring.boot.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@Order(-100) //这是为了保证AOP在事务注解之前生效,Order的值越小,优先级越高
public class ControllerMethodAspect {

    @Pointcut("execution(* com.spring.boot.aop.controller.SampleController.compute(..))")
    private void doComputePointcut() {
    }

    @Before(value = "doComputePointcut() && args(tenantId, companyId, salaryMonth)", argNames = "tenantId,companyId,salaryMonth")
    public String doComputeBeforeAdvice(String tenantId, String companyId, String salaryMonth) {
        log.debug("=== ControllerMethodAspect do Compute Before tenantId: {} companyId: {} salaryMonth: {}", tenantId, companyId, salaryMonth);
        return "doComputeBeforeAdvice return";
    }

    @AfterReturning(value = "doComputePointcut() && args(tenantId, companyId, salaryMonth)", argNames = "tenantId,companyId,salaryMonth")
    public void doComputeAfterReturningAdvice(String tenantId, String companyId, String salaryMonth) {
        log.debug("=== ControllerMethodAspect do Compute After Returning tenantId: {} companyId: {} salaryMonth: {}", tenantId, companyId, salaryMonth);
    }

    @AfterThrowing(value = "doComputePointcut() && args(tenantId, companyId, salaryMonth)", argNames = "tenantId,companyId,salaryMonth")
    public void doComputeAfterThrowingAdvice(String tenantId, String companyId, String salaryMonth) {
        log.debug("=== ControllerMethodAspect do Compute After Throwing tenantId: {} companyId: {} salaryMonth: {}", tenantId, companyId, salaryMonth);
    }

    @Around(value = "doComputePointcut() && args(tenantId, companyId, salaryMonth)", argNames = "pjp,tenantId,companyId,salaryMonth")
    public Object doComputeAroundAdvice(ProceedingJoinPoint pjp, String tenantId, String companyId, String salaryMonth) throws Throwable {
        log.debug("=== ControllerMethodAspect doComputeAroundAdvice tenantId: {} companyId: {} salaryMonth: {}", tenantId, companyId, salaryMonth);

        // 从Redis中获取操作状态，若该参数指定的方法正在执行，则返回，不再执行

//        if (ObjectUtils.nullSafeEquals(tenantId, "tenant_001")) {
//            return "操作正在进行，请稍后！";
//        }

        // start stopwatch
        Object retVal = pjp.proceed();

        // 出现异常，此次不再正常返回，需配合 AfterThrowing 使用，或者在 Controller 中捕获异常
        log.debug("=== ControllerMethodAspect doComputeAroundAdvice return value: {}", retVal);

        // stop stopwatch
        return retVal;
    }

}
