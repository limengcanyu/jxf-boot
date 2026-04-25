package org.asura.i18n.enums;

import org.asura.i18n.utils.I18nEnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Locale;
import java.util.Objects;

/**
 * 通用国际化枚举接口
 */
public interface I18nEnum {
    Logger log = LoggerFactory.getLogger(I18nEnum.class);

    /**
     * 获取国际化消息编码（必须非空）
     */
    String getMessageCode();

    /**
     * 自动适配上下文Locale的描述
     */
    default String getDesc() {
        String code = this.getMessageCode();
        Objects.requireNonNull(code, "枚举 " + this + " 的messageCode不能为空");
        return I18nEnumUtils.getDesc(this);
    }

    /**
     * 带动态参数的描述
     */
    default String getDescWithArgs(Object... args) {
        String code = this.getMessageCode();
        Objects.requireNonNull(code, "枚举 " + this + " 的messageCode不能为空");
        return I18nEnumUtils.getDescWithArgs(this, args);
    }

    /**
     * 手动指定Locale的描述
     */
    default String getDesc(Locale locale) {
        Objects.requireNonNull(locale, "Locale 参数不能为空");
        I18nEnumUtils.withLocale(locale, this::getDesc);
        return this.getDesc();
    }

    /**
     * 校验编码是否有效（存在于资源文件）
     */
    default boolean isCodeValid() {
        try {
            String desc = this.getDesc();
            return !desc.equals(this.getMessageCode());
        } catch (Exception e) {
            log.error("校验枚举[{}]编码有效性失败", this, e);
            return false;
        }
    }
}

