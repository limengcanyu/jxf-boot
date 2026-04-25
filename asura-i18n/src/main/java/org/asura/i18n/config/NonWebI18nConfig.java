package org.asura.i18n.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * 非Web环境（定时/异步任务）国际化配置（最终修正版）
 * 已修复：ThreadPoolTaskScheduler 无 setCorePoolSize() 方法
 */
@Configuration
@EnableScheduling
@EnableAsync
public class NonWebI18nConfig {

    /**
     * 定时任务调度器配置（核心修正：ThreadPoolTaskScheduler 正确配置）
     */
    @Bean
    public SchedulingConfigurer schedulingConfigurer() {
        return taskRegistrar -> {
            // 定时任务专用调度器（ThreadPoolTaskScheduler）
            ThreadPoolTaskScheduler scheduler = taskScheduler();
            taskRegistrar.setScheduler(scheduler);
            // 设置定时任务线程默认Locale
            LocaleContextHolder.setDefaultLocale(Locale.of("zh", "CN"));
        };
    }

    /**
     * 定时任务调度器（ThreadPoolTaskScheduler 正确配置）
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("scheduled-");
        // 核心修正：ThreadPoolTaskScheduler 用 setPoolSize() 替代 setCorePoolSize()
        scheduler.setPoolSize(5); // 线程池大小（核心=最大）
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 定时任务线程初始化时设置默认Locale
        scheduler.setThreadFactory(runnable -> {
            Thread thread = new Thread(runnable);
            LocaleContextHolder.setLocale(Locale.of("zh", "CN"));
            return thread;
        });
        scheduler.initialize();
        return scheduler;
    }

    /**
     * 异步任务执行器（ThreadPoolTaskExecutor 原有配置保留）
     */
    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setThreadNamePrefix("async-");
                // ThreadPoolTaskExecutor 可正常使用 setCorePoolSize/setMaxPoolSize
                executor.setCorePoolSize(10);
                executor.setMaxPoolSize(20);
                executor.setAwaitTerminationSeconds(60);
                executor.setWaitForTasksToCompleteOnShutdown(true);
                // 异步任务线程初始化时设置默认Locale
                executor.setThreadFactory(runnable -> {
                    Thread thread = new Thread(runnable);
                    LocaleContextHolder.setLocale(Locale.of("zh", "CN"));
                    return thread;
                });
                executor.initialize();
                return executor;
            }
        };
    }

    /**
     * 单元测试Locale工具
     */
    public static class TestI18nUtil {
        public static void setTestLocale(Locale locale) {
            LocaleContextHolder.setLocale(locale);
        }

        public static void resetTestLocale() {
            LocaleContextHolder.resetLocaleContext();
        }
    }

}

