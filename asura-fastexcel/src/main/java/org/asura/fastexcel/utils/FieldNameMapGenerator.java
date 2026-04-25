package org.asura.fastexcel.utils;

import org.asura.fastexcel.vo.OrderExcelFixedColumnVO;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字段名映射工具类
 * 功能：生成 {字段名: 字段名+指定后缀} 的Map，默认后缀为ColumnIndex
 */
public class FieldNameMapGenerator {

    /**
     * 核心方法：生成字段名映射Map（自定义后缀）
     *
     * @param clazz  目标类（如ExcelFixedColumnVO.class）
     * @param suffix 字段名后缀（如"ColumnIndex"）
     * @return Map<字段名, 字段名+后缀>，保留字段定义顺序
     * @throws IllegalArgumentException 入参非法时抛出
     */
    public static Map<String, String> generateFieldNameMap(Class<?> clazz, String suffix) {
        // 空值校验
        if (clazz == null) {
            throw new IllegalArgumentException("目标类不能为空");
        }
        String actualSuffix = (suffix == null) ? "" : suffix;

        // 使用LinkedHashMap保证字段顺序与类中定义一致
        Map<String, String> fieldNameMap = new LinkedHashMap<>();

        // 获取类的所有声明字段（包括private，不包含父类字段）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            // 拼接：字段名 + 后缀
            String fieldNameWithSuffix = fieldName + actualSuffix;
            fieldNameMap.put(fieldName, fieldNameWithSuffix);
        }

        // 递归获取父类字段（直到Object类）
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Map<String, String> superFieldMap = generateFieldNameMap(superClass, suffix);
            fieldNameMap.putAll(superFieldMap); // 父类字段先放入，子类字段后放入（覆盖重复字段名）
        }

        return fieldNameMap;
    }

    /**
     * 重载方法：默认后缀为"ColumnIndex"（简化调用）
     *
     * @param clazz 目标类
     * @return Map<字段名, 字段名+ColumnIndex>
     */
    public static Map<String, String> generateFieldNameMap(Class<?> clazz) {
        return generateFieldNameMap(clazz, "ColumnIndex");
    }

    // 测试示例（以ExcelFixedColumnVO为例）
    public static void main(String[] args) {
        // 生成ExcelFixedColumnVO的字段名映射Map
//        Map<String, String> resultMap = generateFieldNameMap(ExcelFixedColumnVO.class);
        Map<String, String> resultMap = generateFieldNameMap(OrderExcelFixedColumnVO.class);

        // 打印结果验证
        resultMap.forEach((fieldName, fieldNameWithSuffix) -> {
//            System.out.println(fieldName + " → " + fieldNameWithSuffix);
            System.out.println("result.put(\"" + fieldName + "\", \"" + fieldNameWithSuffix + "\");");
        });

//        // 1. 默认后缀（ColumnIndex）
//        Map<String, String> defaultMap = FieldNameMapGenerator.generateFieldNameMap(ExcelFixedColumnVO.class);
//        System.out.println(defaultMap);
//
//        // 2. 自定义后缀（如"Index"）
//        Map<String, String> customMap = FieldNameMapGenerator.generateFieldNameMap(ExcelFixedColumnVO.class, "Index");
//        System.out.println(customMap);
    }
}

