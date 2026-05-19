package org.asura.fastexcel.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类 - 一次性缓存类的所有字段，提升性能
 */
@Slf4j
public class ReflectCacheUtil {
    // 缓存：Class -> (字段名 -> Field)
    private static final Map<Class<?>, Map<String, Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 初始化类的所有字段缓存（一次性加载）
     */
    private static void initFieldCache(Class<?> clazz) {
        // 已缓存则直接返回
        if (CLASS_FIELD_CACHE.containsKey(clazz)) {
            return;
        }
        Map<String, Field> fieldMap = new HashMap<>();
        // 一次性获取类的所有声明字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        // 存入全局缓存
        CLASS_FIELD_CACHE.put(clazz, fieldMap);
        log.info("类{}的字段缓存初始化完成，共缓存{}个字段", clazz.getName(), fieldMap.size());
    }

    /**
     * 初始化类的所有字段缓存（一次性加载）
     */
    private static void initFieldAndParentCache(Class<?> clazz) {
        // 已缓存则直接返回
        if (CLASS_FIELD_CACHE.containsKey(clazz)) {
            return;
        }
        Map<String, Field> fieldMap = new HashMap<>();
        while (clazz != Object.class) {
            // 一次性获取类的所有声明字段
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        }
        // 存入全局缓存
        CLASS_FIELD_CACHE.put(clazz, fieldMap);
        log.info("类{}的字段缓存初始化完成，共缓存{}个字段", clazz.getName(), fieldMap.size());
    }

    /**
     * 获取类的指定字段（基于全量缓存）
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Assert.notNull(clazz, "Class不能为空");
        Assert.notNull(fieldName, "字段名不能为空");
        // 确保缓存已初始化
        initFieldCache(clazz);
        // 从缓存获取字段
        Field field = CLASS_FIELD_CACHE.get(clazz).get(fieldName);
        if (field == null) {
            throw new RuntimeException(String.format("类%s不存在字段%s", clazz.getName(), fieldName));
        }
        return field;
    }

    /**
     * 设置对象的字段值（基于全量缓存的反射）
     */
    public static void setFieldValue(Object target, String fieldName, Object value) {
        Assert.notNull(target, "目标对象不能为空");
        try {
            Field field = getField(target.getClass(), fieldName);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("设置字段{}的值失败", fieldName, e);
            throw new RuntimeException(String.format("设置字段%s的值失败", fieldName));
        }
    }

    /**
     * 手动清除指定类的字段缓存（适用于类结构动态变更场景）
     */
    public static void clearFieldCache(Class<?> clazz) {
        CLASS_FIELD_CACHE.remove(clazz);
        log.info("类{}的字段缓存已清除", clazz.getName());
    }

    public static void print() {
        log.info(JSONUtil.toJsonPrettyStr(CLASS_FIELD_CACHE));
    }

    /**
     * 反射设置任意父类的字段值
     * @param target 目标对象（子类实例）
     * @param fieldName 要设置的字段名（父类中）
     * @param value 要设置的值
     * @throws Exception 字段未找到/访问异常
     */
    public static void setParentField(Object target, String fieldName, Object value) throws Exception {
        Class<?> currentClass = target.getClass();
        // 循环查找所有父类（直到Object）
        while (currentClass != Object.class) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return; // 找到并设置后退出
            } catch (NoSuchFieldException e) {
                // 当前类没有该字段，继续找父类
                currentClass = currentClass.getSuperclass();
            }
        }
        // 所有父类都没找到字段
        throw new NoSuchFieldException("字段" + fieldName + "在所有父类中未找到");
    }

    static void main() throws Exception {
////        ReflectCacheUtil.initFieldCache(ExcelFixedColumnVO.class);
//        ReflectCacheUtil.initFieldAndParentCache(ExcelFixedColumnVO.class);
//        ReflectCacheUtil.print();
//
//        ExcelFixedColumnVO excelFixedColumnVO = new ExcelFixedColumnVO();
//        ReflectCacheUtil.setFieldValue(excelFixedColumnVO, "project", "pro01");
//        ReflectCacheUtil.setFieldValue(excelFixedColumnVO, "stage", "st01");
//        ReflectCacheUtil.setFieldValue(excelFixedColumnVO, "identifier", "id01");
//        ReflectCacheUtil.setFieldValue(excelFixedColumnVO, "rowNum", 1);
////        ReflectCacheUtil.setParentField(excelFixedColumnVO, "rowNum", 1);
//        log.info(JSONUtil.toJsonPrettyStr(excelFixedColumnVO));
    }
}
