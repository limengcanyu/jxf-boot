package com.spring.boot.mybatis.plus.aspects;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.spring.boot.mybatis.plus.controller..*.*(..)) || execution(* com.spring.boot.mybatis.plus.service..*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder();
        int length = args.length;
        for (int i = 0; i < length; i++) {
            if (null != args[i] && !(args[i] instanceof HttpServletRequest) && !(args[i] instanceof HttpServletResponse) && !(args[i] instanceof MultipartFile)) {
                sb.append("[").append(i).append("]: ").append(args[i]).append("\n");
            }
        }
        // 去掉最后的换行符
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }

        log.info("\n开始执行方法: {} \n参数: {}", signature, sb);

        try {
            Object result = joinPoint.proceed();
            log.info("\n结束执行方法: {} \n结果: {}", signature, JSONObject.toJSONString(result));
            return result;
        } catch (Throwable e) {
            log.info("\n结束执行方法: {} \n异常: ", signature, e);
            throw new RuntimeException(e);
        }
    }

}
