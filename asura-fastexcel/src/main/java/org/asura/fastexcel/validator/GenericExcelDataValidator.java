package org.asura.fastexcel.validator;


import org.asura.fastexcel.utils.FastFieldSetter;
import org.asura.fastexcel.vo.ExcelDataVO;
import org.asura.fastexcel.vo.ExcelDynamicColumnAble;
import org.asura.fastexcel.vo.ExcelFixedColumnAble;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Excel数据校验工具类
 */
public class GenericExcelDataValidator {

    // 初始化Validator（JSR-380标准）
    private static final Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();

        // JVM关闭时释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(factory::close));
    }

    /**
     * 批量校验Excel解析结果
     * @param excelDataList 解析后的Excel数据列表
     * @return 校验结果：key=行号，value=该行的错误信息列表；无错误则返回空Map
     */
    public static <T extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> Map<Integer, List<String>> validateExcelData(List<ExcelDataVO<T, D>> excelDataList) {
        Map<Integer, List<String>> errorMap = new HashMap<>();

        // 逐行校验
        for (ExcelDataVO<T, D> excelData : excelDataList) {
            int rowNum = excelData.getRowNum();
            List<String> rowErrors = new ArrayList<>();

            // 1. 校验固定列
            Set<ConstraintViolation<T>> fixedViolations = VALIDATOR.validate(excelData.getFixedColumn());
            if (!fixedViolations.isEmpty()) {
                rowErrors.addAll(fixedViolations.stream().map(ConstraintViolation::getMessage).toList());
            }

            // 固定列校验通过再校验动态列
            if (CollectionUtils.isEmpty(rowErrors)) {
                // 2. 校验动态列（遍历每个动态列）
                List<D> dynamicColumns = excelData.getDynamicColumns();
                if (Objects.nonNull(dynamicColumns) && !dynamicColumns.isEmpty()) {
                    for (int i = 0; i < dynamicColumns.size(); i++) {
                        D dynamicVO = dynamicColumns.get(i);
                        Set<ConstraintViolation<D>> dynamicViolations = VALIDATOR.validate(dynamicVO);
                        if (!dynamicViolations.isEmpty()) {
//                            // 根据动态列索引显示错误
//                            int finalI = i;
//                            rowErrors.addAll(dynamicViolations.stream()
//                                    .map(violation -> String.format("动态列[%s]：%s", finalI + 1, violation.getMessage()))
//                                    .toList());

                            // 根据动态列head显示错误
                            rowErrors.addAll(dynamicViolations.stream()
                                    .map(violation -> String.format("动态列[%s]：%s", FastFieldSetter.getFieldValue(dynamicVO, dynamicVO.getDynamicColumnFieldName().get("head")), violation.getMessage()))
                                    .toList());
                        }
                    }
                }
            }

            // 3. 收集当前行的错误信息
            if (!rowErrors.isEmpty()) {
                errorMap.put(rowNum, rowErrors);
            }

            // 如果错误行达到2条，则直接返回错误，不再继续校验
            if (errorMap.size() >= 2) {
                return errorMap;
            }
        }

        return errorMap;
    }

    /**
     * 快速校验（有错误直接抛出异常）
     * @param excelDataList 解析后的Excel数据列表
     * @throws IllegalArgumentException 校验失败异常（包含所有错误信息）
     */
    public static <T extends ExcelFixedColumnAble, D extends ExcelDynamicColumnAble> void validateAndThrow(List<ExcelDataVO<T, D>> excelDataList) {
        Map<Integer, List<String>> errorMap = validateExcelData(excelDataList);
        if (!errorMap.isEmpty()) {
            // 拼接错误信息（行号+错误）
            StringBuilder errorMsg = new StringBuilder("Excel数据校验失败：\n");
            errorMap.forEach((rowNum, errors) -> errorMsg.append("第").append(rowNum).append("行：").append(String.join("；", errors)).append("\n"));
            throw new IllegalArgumentException(errorMsg.toString());
        }
    }
}

