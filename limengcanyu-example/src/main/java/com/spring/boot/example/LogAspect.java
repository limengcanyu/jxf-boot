package com.spring.boot.example;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
//@Component
public class LogAspect {

    @Pointcut("execution(* com.spring.boot.example..*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {

        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg.toString()).append("\n");
        }

        log.info("call method: {} args: {}", signature, sb);

        try {
            Object result = joinPoint.proceed();
            log.info("call method: {} result: {}", signature, JSONObject.toJSONString(result));
            return result;
        } catch (Throwable e) {
            log.info("call method: {} exception: ", signature, e);
            throw new RuntimeException(e);
        }

    }

}
