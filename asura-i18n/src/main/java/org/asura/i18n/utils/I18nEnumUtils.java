package org.asura.i18n.utils;

import org.asura.i18n.enums.I18nEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 国际化枚举工具类（生产级优化）
 */
@Component
public class I18nEnumUtils {
    private static final Logger log = LoggerFactory.getLogger(I18nEnumUtils.class);

    @Resource
    private MessageSource messageSource;

    private static volatile I18nEnumUtils INSTANCE;

    @PostConstruct
    public void init() {
        if (INSTANCE != null) {
            log.warn("I18nEnumUtils 已初始化，避免重复注入");
            return;
        }
        INSTANCE = this;
        log.info("I18nEnumUtils 初始化完成，默认Locale：{}", getSafeLocale());
    }

    public static String getDesc(@NonNull I18nEnum i18nEnum) {
        Objects.requireNonNull(i18nEnum, "I18nEnum 参数不能为空");
        String code = i18nEnum.getMessageCode();
        Objects.requireNonNull(code, "枚举 " + i18nEnum + " 的messageCode不能为空");

        try {
            return getDescWithArgs(i18nEnum, null);
        } catch (Exception e) {
            log.error("获取枚举[{}]国际化描述失败，code：{}", i18nEnum, code, e);
            return code;
        }
    }

    public static String getDescWithArgs(@NonNull I18nEnum i18nEnum, @Nullable Object[] args) {
        Objects.requireNonNull(i18nEnum, "I18nEnum 参数不能为空");
        String code = i18nEnum.getMessageCode();
        Objects.requireNonNull(code, "枚举 " + i18nEnum + " 的messageCode不能为空");

        Locale currentLocale = getSafeLocale();
        try {
            if (INSTANCE == null) {
                log.warn("I18nEnumUtils 未初始化，使用默认Locale：{}", currentLocale);
                return code;
            }
            return INSTANCE.messageSource.getMessage(code, args, currentLocale);
        } catch (Exception e) {
            log.error("获取枚举[{}]带参数国际化描述失败，code：{}，args：{}，locale：{}",
                    i18nEnum, code, args, currentLocale, e);
            return code;
        }
    }

    public static <T extends I18nEnum> List<String> getDescList(@Nullable List<T> enumList) {
        if (enumList == null || enumList.isEmpty()) {
            log.debug("枚举列表为空，返回空集合");
            return List.of();
        }

        Locale currentLocale = getSafeLocale();
        return enumList.stream()
                .filter(Objects::nonNull)
                .map(enumItem -> {
                    try {
                        String code = enumItem.getMessageCode();
                        return INSTANCE.messageSource.getMessage(code, null, currentLocale);
                    } catch (Exception e) {
                        log.error("批量获取枚举[{}]描述失败，locale：{}", enumItem, currentLocale, e);
                        return enumItem.getMessageCode();
                    }
                })
                .collect(Collectors.toList());
    }

    public static void withLocale(@NonNull Locale locale, @NonNull Runnable task) {
        Objects.requireNonNull(locale, "Locale 参数不能为空");
        Objects.requireNonNull(task, "Runnable 任务不能为空");

        Locale originalLocale = getSafeLocale();
        log.debug("切换线程Locale：{} → {}", originalLocale, locale);
        try {
            LocaleContextHolder.setLocale(locale);
            task.run();
        } catch (Exception e) {
            log.error("执行带Locale的异步任务失败", e);
            throw e;
        } finally {
            LocaleContextHolder.setLocale(originalLocale);
            log.debug("恢复线程Locale：{}", originalLocale);
        }
    }

    private static Locale getSafeLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().isEmpty()) {
            log.debug("上下文Locale为空，使用默认Locale：en");
            return Locale.of("en");
        }
        return locale;
    }
}

