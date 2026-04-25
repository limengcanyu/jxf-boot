package org.asura.fastexcel.utils;


import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高效反射字段赋值工具类
 * 特性：
 * 1. 缓存类的字段元数据（包括父类），避免重复反射查找
 * 2. 提前取消字段访问检查，提升赋值效率
 * 3. 支持批量赋值、单个字段赋值
 * 4. 线程安全，适合高并发场景
 */
public class FastFieldSetter {

    // 字段缓存：key=类名+字段名，value=对应的Field对象（已取消访问检查）
    private static final ConcurrentHashMap<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    // 类的所有字段缓存：key=Class，value=该类所有字段（含父类）的名称集合
    private static final ConcurrentHashMap<Class<?>, Set<String>> CLASS_FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * 初始化类的字段缓存（递归查找父类）
     * @param clazz 目标类
     */
    private static void initClassFields(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return;
        }
        // 避免重复初始化
        if (CLASS_FIELDS_CACHE.containsKey(clazz)) {
            return;
        }

        Set<String> fieldNames = new HashSet<>();
        // 获取当前类的所有声明字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String key = getFieldCacheKey(clazz, field.getName());
            // 取消访问检查（核心优化点）
            field.setAccessible(true);
            FIELD_CACHE.put(key, field);
            fieldNames.add(field.getName());
        }

        // 递归处理父类
        initClassFields(clazz.getSuperclass());
        // 合并父类字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            Set<String> superFieldNames = CLASS_FIELDS_CACHE.get(superClass);
            if (superFieldNames != null) {
                fieldNames.addAll(superFieldNames);
            }
        }

        CLASS_FIELDS_CACHE.put(clazz, fieldNames);
    }

    /**
     * 获取字段缓存的key
     */
    private static String getFieldCacheKey(Class<?> clazz, String fieldName) {
        return clazz.getName() + "#" + fieldName;
    }

    /**
     * 查找字段（优先从缓存获取，支持父类）
     */
    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        // 初始化缓存
        initClassFields(clazz);

        // 先查当前类
        String key = getFieldCacheKey(clazz, fieldName);
        Field field = FIELD_CACHE.get(key);
        if (field != null) {
            return field;
        }

        // 递归查父类
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getField(superClass, fieldName);
        }

        throw new NoSuchFieldException("字段不存在：" + clazz.getName() + "." + fieldName);
    }

    /**
     * 给单个对象设置单个字段值
     * @param target 目标对象
     * @param fieldName 字段名
     * @param value 字段值
     */
    public static void setFieldValue(Object target, String fieldName, Object value) {
        if (target == null || fieldName == null) {
            throw new IllegalArgumentException("目标对象或字段名不能为空");
        }
        Class<?> clazz = target.getClass();
        try {
            Field field = getField(clazz, fieldName);

            if (field.getType() == Long.class || field.getType() == long.class) {
                field.set(target, Long.parseLong(value.toString()));
            } else if (field.getType() == Integer.class || field.getType() == int.class) {
                field.set(target, Integer.parseInt(value.toString()));
            } else {
                field.set(target, value);
            }

        } catch (NoSuchFieldException e) {
            throw new RuntimeException("字段不存在：" + fieldName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("字段赋值失败：" + fieldName, e);
        }
    }

    /**
     * 获取单个对象单个字段值
     * @param target 目标对象
     * @param fieldName 字段名
     * @return 字段值
     */
    public static Object getFieldValue(Object target, String fieldName) {
        if (target == null || fieldName == null) {
            throw new IllegalArgumentException("目标对象或字段名不能为空");
        }
        Class<?> clazz = target.getClass();
        try {
            Field field = getField(clazz, fieldName);

            return field.get(target);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("字段不存在：" + fieldName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("字段赋值失败：" + fieldName, e);
        }
    }

    /**
     * 批量设置对象字段值
     * @param target 目标对象
     * @param fieldValues 字段名-值映射
     */
    public static void batchSetFieldValues(Object target, Map<String, Object> fieldValues) {
        if (target == null || fieldValues == null || fieldValues.isEmpty()) {
            return;
        }
        Class<?> clazz = target.getClass();
        initClassFields(clazz);

        for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = getField(clazz, fieldName);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException("批量赋值失败，字段：" + fieldName, e);
            }
        }
    }

    /**
     * 清空指定类的字段缓存（用于类加载器刷新场景）
     */
    public static void clearCache(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        // 移除类的字段名缓存
        CLASS_FIELDS_CACHE.remove(clazz);
        // 移除该类所有字段的缓存
        String className = clazz.getName();
        FIELD_CACHE.keySet().removeIf(key -> key.startsWith(className + "#"));
        // 递归清空父类缓存
        clearCache(clazz.getSuperclass());
    }

    /**
     * 清空所有缓存
     */
    public static void clearAllCache() {
        FIELD_CACHE.clear();
        CLASS_FIELDS_CACHE.clear();
    }

    // ====================== 测试用例 ======================
    static class Parent {
        private Long parentId;
        private String parentName;

        @Override
        public String toString() {
            return "Parent{parentId=" + parentId + ", parentName='" + parentName + "'}";
        }
    }

    static class Child extends Parent {
        private Long childId;
        private String childName;

        @Override
        public String toString() {
            return "Child{childId=" + childId + ", childName='" + childName + "'} " + super.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. 基础功能测试
        Child child = new Child();
        // 设置子类字段
        setFieldValue(child, "childId", 1001L);
        setFieldValue(child, "childName", "测试子类");
        // 设置父类字段
        setFieldValue(child, "parentId", 2001L);
        setFieldValue(child, "parentName", "测试父类");
        System.out.println("基础赋值结果：" + child);

        // 2. 批量赋值测试
        Child batchChild = new Child();
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("childId", 1002L);
        fieldMap.put("childName", "批量子类");
        fieldMap.put("parentId", 2002L);
        fieldMap.put("parentName", "批量父类");
        batchSetFieldValues(batchChild, fieldMap);
        System.out.println("批量赋值结果：" + batchChild);

        // 3. 性能测试（100万次赋值）
        long start = System.currentTimeMillis();
        Child perfChild = new Child();
        for (int i = 0; i < 1_000_000; i++) {
            setFieldValue(perfChild, "childId", (long) i);
            setFieldValue(perfChild, "parentId", (long) (i + 1000000));
        }
        long end = System.currentTimeMillis();
        System.out.println("100万次赋值耗时：" + (end - start) + "ms");
    }
}

