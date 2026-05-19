package org.asura.i18n.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * 国际化核心配置类（解决 Accept-Language 无法修改的报错）
 */
@Slf4j
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultLocale(Locale.of("en"));
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * 核心修正：自定义 LocaleResolver，不继承 AcceptHeaderLocaleResolver
     * 手动实现「lang 参数 + Accept-Language + 默认 Locale」的解析逻辑
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {
            // 支持的语言列表
            private final Locale[] SUPPORTED_LOCALES = {Locale.of("en"), Locale.of("zh", "CN")};
            // 默认语言
            private final Locale DEFAULT_LOCALE = Locale.of("en");

            @NonNull
            @Override
            public Locale resolveLocale(@NonNull HttpServletRequest request) {
                // 步骤1：优先解析 lang 参数（优先级最高）
                String langParam = request.getParameter("lang");
                if (langParam != null && !langParam.trim().isEmpty()) {
                    Locale paramLocale = resolveLangParam(langParam);
                    // 同步到 LocaleContextHolder
                    LocaleContextHolder.setLocale(paramLocale);
                    return paramLocale;
                }

                // 步骤2：解析 Accept-Language 请求头
                String acceptLanguage = request.getHeader("Accept-Language");
                if (acceptLanguage != null && !acceptLanguage.trim().isEmpty()) {
                    // 解析请求头（如 "zh-CN,zh;q=0.9,en;q=0.8"）
                    String[] locales = acceptLanguage.split(",");
                    for (String localeStr : locales) {
                        // 去掉权重（如 ;q=0.9）
                        String[] parts = localeStr.trim().split(";")[0].split("-");
                        try {
                            Locale headerLocale;
                            if (parts.length == 2) {
                                headerLocale = Locale.of(parts[0], parts[1]);
                            } else if (parts.length == 1) {
                                headerLocale = Locale.of(parts[0]);
                            } else {
                                continue;
                            }
                            // 校验是否在支持的列表中
                            for (Locale supported : SUPPORTED_LOCALES) {
                                if (supported.getLanguage().equals(headerLocale.getLanguage())) {
                                    LocaleContextHolder.setLocale(supported);
                                    return supported;
                                }
                            }
                        } catch (Exception e) {
                            // 解析失败，继续下一个
                            log.error("解析 Accept-Language 请求头失败：", e);
                        }
                    }
                }

                // 步骤3：兜底返回默认 Locale
                LocaleContextHolder.setLocale(DEFAULT_LOCALE);
                return DEFAULT_LOCALE;
            }

            @Override
            public void setLocale(@NonNull HttpServletRequest request, HttpServletResponse response, Locale locale) {
                // 支持手动设置 Locale（不再抛出异常）
                if (locale != null) {
                    // 校验 Locale 是否合法
                    for (Locale supported : SUPPORTED_LOCALES) {
                        if (supported.equals(locale)) {
                            LocaleContextHolder.setLocale(locale);
                            return;
                        }
                    }
                    // 非法 Locale 用默认值
                    LocaleContextHolder.setLocale(DEFAULT_LOCALE);
                }
            }

            /**
             * 解析 lang 参数（支持 en / zh-CN 格式）
             */
            @NonNull
            private Locale resolveLangParam(@NonNull String lang) {
                String[] parts = lang.trim().split("-");
                try {
                    if (parts.length == 2) {
                        Locale locale = Locale.of(parts[0], parts[1]);
                        for (Locale supported : SUPPORTED_LOCALES) {
                            if (supported.equals(locale)) {
                                return locale;
                            }
                        }
                    } else if (parts.length == 1) {
                        Locale locale = Locale.of(parts[0]);
                        for (Locale supported : SUPPORTED_LOCALES) {
                            if (supported.getLanguage().equals(locale.getLanguage())) {
                                return supported;
                            }
                        }
                    }
                } catch (Exception e) {
                    // 解析失败，返回默认值
                    log.error("解析 lang 参数失败：", e);
                }
                return DEFAULT_LOCALE;
            }
        };
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * 全局响应头拦截器：统一设置Content-Type为application/json;charset=UTF-8
     */
    @Bean
    public ResponseContentTypeInterceptor responseContentTypeInterceptor() {
        return new ResponseContentTypeInterceptor();
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(responseContentTypeInterceptor()).addPathPatterns("/**");
    }

    @Bean("customStringHttpMessageConverter")
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    /**
     * 全局响应头拦截器
     */
    public static class ResponseContentTypeInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws Exception {
            response.setContentType("application/json;charset=UTF-8");
            return true;
        }
    }

}

