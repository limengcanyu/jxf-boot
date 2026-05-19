package org.asura.i18n.controller;

import org.asura.i18n.enums.BusinessStatusEnum;
import org.asura.i18n.enums.BusinessTypeEnum;
import org.asura.i18n.enums.CommonErrorEnum;
import org.asura.i18n.exception.BusinessException;
import org.asura.i18n.utils.I18nEnumUtils;
import org.asura.i18n.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 国际化测试控制器（补充带参数的国际化示例）
 */
@RestController
public class EnumI18nController {

    /**
     * 原有：基础枚举国际化（无参数）
     */
    @GetMapping("/enum-desc")
    public Result<String> getEnumDesc() {
        String successDesc = BusinessStatusEnum.SUCCESS.getDesc();
        String payDesc = BusinessTypeEnum.PAY.getDesc();
        // 原有：未传参的带参数方法（无意义）
        // String successWithArgs = BusinessStatusEnum.SUCCESS.getDescWithArgs("1001");
        String result = String.format("状态：%s | 类型：%s", successDesc, payDesc);
        return Result.success(result);
    }

    /**
     * 新增：带动态参数的国际化测试（核心示例）
     * 示例：/enum-desc-param?operate=支付&orderNo=20260107001&amount=99
     */
    @GetMapping("/enum-desc-param")
    public Result<String> getEnumDescWithParam(@RequestParam String operate, @RequestParam String orderNo, @RequestParam Integer amount) {
        // 1. 带2个参数的国际化文案：操作{0}成功，订单号：{1}
        String successWithParam = BusinessStatusEnum.SUCCESS_WITH_PARAM.getDescWithArgs(operate, orderNo);
        // 2. 带1个参数的国际化文案：支付金额：{0} 元
        String payWithParam = BusinessTypeEnum.PAY_WITH_PARAM.getDescWithArgs(amount);

        String result = String.format("%s | %s", successWithParam, payWithParam);
        return Result.success(result);
    }

    /**
     * 原有：批量枚举描述
     */
    @GetMapping("/enum-desc-list")
    public Result<List<String>> getEnumDescList() {
        List<String> descList = I18nEnumUtils.getDescList(Arrays.asList(BusinessStatusEnum.values()));
        return Result.success(descList);
    }

    /**
     * 原有：异常国际化
     */
    @GetMapping("/enum-exception")
    public Result<?> testException() {
        throw new BusinessException(CommonErrorEnum.PARAM_ERROR);
    }

    /**
     * 原有：手动指定lang参数
     */
    @GetMapping("/enum-desc-lang")
    public Result<String> getEnumDescByLang(@RequestParam(required = false) String lang) {
        String failedDesc = BusinessStatusEnum.FAILED.getDesc(Locale.of(lang));
        return Result.success("手动指定语言描述：" + failedDesc);
    }
}

