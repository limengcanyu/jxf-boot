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
public class ControllerAspect {

    @Pointcut("execution(* com.spring.boot.aop.controller.SampleController.*(..))")
    private void doComputeAspectPointcut() {
    }

    @Before("doComputeAspectPointcut()")
    public void doComputeBeforeAdvice() {
        log.debug("=== ControllerAspect do Compute Before");
    }

    @AfterReturning("doComputeAspectPointcut()")
    public void doComputeAfterReturningAdvice() {
        log.debug("=== ControllerAspect do Compute After Returning");
    }

    @AfterThrowing("doComputeAspectPointcut()")
    public void doComputeAfterThrowingAdvice() {
        log.debug("=== ControllerAspect do Compute After Throwing");
    }

    @Around("doComputeAspectPointcut()")
    public Object doComputeAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("=== ControllerAspect do Compute Around");

        // start stopwatch
        Object retVal = pjp.proceed();

        // 遇到异常不会执行以下语句，可以通过添加 AfterThrowing 处理异常情况，或者在 Controller 中捕获异常，就无需再加 AfterThrowing
        log.debug("=== ControllerAspect do Compute Around return value: {}", retVal);

        // stop stopwatch
        return retVal;
    }

}
