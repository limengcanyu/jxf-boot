package org.asura.i18n.aspect;

import org.asura.i18n.enums.BusinessStatusEnum;
import org.asura.i18n.enums.BusinessTypeEnum;
import org.asura.i18n.enums.CommonErrorEnum;
import org.asura.i18n.enums.I18nEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 国际化监控切面 + 启动时枚举编码校验
 */
@Aspect
@Component
public class I18nMonitorAspect implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(I18nMonitorAspect.class);

    /**
     * 监控国际化解析方法耗时
     */
    @Around("execution(* org.asura.i18n.utils.I18nEnumUtils.getDesc(..)) || " +
            "execution(* org.asura.i18n.enums.I18nEnum.getDesc(..))")
    public Object monitorI18n(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long cost = System.currentTimeMillis() - start;
            if (cost > 10) {
                log.warn("国际化解析耗时过长：{}ms，方法：{}，参数：{}", cost, joinPoint.getSignature(), joinPoint.getArgs());
            } else {
                log.debug("国际化解析耗时：{}ms，方法：{}", cost, joinPoint.getSignature());
            }
            return result;
        } catch (Exception e) {
            log.error("国际化解析异常，方法：{}，参数：{}", joinPoint.getSignature(), joinPoint.getArgs(), e);
            throw e;
        }
    }

    /**
     * 启动时校验所有枚举编码有效性
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始校验枚举国际化编码有效性...");

        // 校验业务状态枚举
        boolean statusValid = Arrays.stream(BusinessStatusEnum.values()).allMatch(I18nEnum::isCodeValid);

        // 校验业务类型枚举
        boolean typeValid = Arrays.stream(BusinessTypeEnum.values()).allMatch(I18nEnum::isCodeValid);

        // 校验通用错误枚举
        boolean errorValid = Arrays.stream(CommonErrorEnum.values()).allMatch(I18nEnum::isCodeValid);

        if (statusValid && typeValid && errorValid) {
            log.info("所有枚举编码校验通过");
        } else {
//            log.error("部分枚举编码不存在于资源文件中，请检查！");
            // 生产环境可抛出异常阻止启动
            throw new RuntimeException("枚举国际化编码校验失败");
        }
    }
}

