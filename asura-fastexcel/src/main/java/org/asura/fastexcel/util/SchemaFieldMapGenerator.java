package org.asura.fastexcel.util;

import io.swagger.v3.oas.annotations.media.Schema;
import org.asura.fastexcel.vo.OrderExcelFixedColumnVO;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel字段注解解析工具类
 * 功能：解析指定VO类的字段@Schema注解，生成{description: 字段名+ColumnIndex}的Map
 */
public class SchemaFieldMapGenerator {

    /**
     * 生成字段注解映射Map
     * @param clazz 目标VO类（如ExcelFixedColumnVO.class）
     * @param suffix 字段名称后缀（固定为"ColumnIndex"，也可自定义）
     * @return Map<Schema.description, 字段名+suffix>，保留字段定义顺序
     */
    public static Map<String, String> generateSchemaFieldMap(Class<?> clazz, String suffix) {
        // 使用LinkedHashMap保证字段定义顺序（与类中字段顺序一致）
        Map<String, String> schemaFieldMap = new LinkedHashMap<>();

        // 空值校验
        if (clazz == null) {
            throw new IllegalArgumentException("目标类不能为空");
        }
        if (suffix == null) {
            suffix = ""; // 后缀为空时直接拼接空字符串
        }

        // 获取类的所有声明字段（包括private）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 读取字段上的@Schema注解
            Schema schema = field.getAnnotation(Schema.class);
            String description;

            // 处理注解缺失的情况（默认使用字段名作为description）
            if (schema == null || schema.description() == null || schema.description().trim().isEmpty()) {
                description = field.getName(); // 注解缺失时，key为字段名
            } else {
                description = schema.description().trim(); // 注解存在时，key为description
            }

            // 拼接字段名 + 后缀（如project → projectColumnIndex）
            String fieldNameWithSuffix = field.getName() + suffix;

            // 放入Map（若有重复description，后面的字段会覆盖前面的）
            schemaFieldMap.put(description, fieldNameWithSuffix);
        }

        // 扩展：解析父类字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Map<String, String> superMap = generateSchemaFieldMap(superClass, suffix);
            schemaFieldMap.putAll(superMap); // 父类字段先放入，子类字段后放入（覆盖父类重复key）
        }

        return schemaFieldMap;
    }

    /**
     * 重载方法：默认后缀为"ColumnIndex"（简化调用）
     * @param clazz 目标VO类
     * @return Map<Schema.description, 字段名+ColumnIndex>
     */
    public static Map<String, String> generateSchemaFieldMap(Class<?> clazz) {
        return generateSchemaFieldMap(clazz, "ColumnIndex");
    }

    // 测试示例
    public static void main(String[] args) {
        // 解析ExcelFixedColumnVO生成Map
//        Map<String, String> resultMap = generateSchemaFieldMap(ExcelFixedColumnVO.class);
        Map<String, String> resultMap = generateSchemaFieldMap(OrderExcelFixedColumnVO.class);

        // 打印结果（验证输出）
        resultMap.forEach((desc, fieldName) -> {
//            System.out.println(desc + " → " + fieldName);
            System.out.println("result.put(\"" + desc + "\", \"" + fieldName + "\");");
        });

//        // 1. 默认后缀（ColumnIndex）
//        Map<String, String> map1 = SchemaFieldMapGenerator.generateSchemaFieldMap(ExcelFixedColumnVO.class);
//        System.out.println(map1);
//        // 输出：{项目=projectColumnIndex, 阶段=stageColumnIndex, 标识=identifierColumnIndex, 备注=remarkColumnIndex}
//
//        // 2. 自定义后缀（如"Index"）
//        Map<String, String> map2 = SchemaFieldMapGenerator.generateSchemaFieldMap(ExcelFixedColumnVO.class, "Index");
//        System.out.println(map2);
//        // 输出：{项目=projectIndex, 阶段=stageIndex, 标识=identifierIndex, 备注=remarkIndex}
    }
}
