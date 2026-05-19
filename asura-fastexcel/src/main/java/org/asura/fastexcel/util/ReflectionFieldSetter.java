package org.asura.fastexcel.util;


import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ReflectionFieldSetter {

    // 缓存 key: className + "#" + fieldName
    private static final Map<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 递归查找字段（包括父类）
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                // 继续向上查找
            }
            current = current.getSuperclass();
        }
        return null; // 字段不存在
    }

    /**
     * 设置对象的字段值（支持私有、父类字段）
     */
    public static void setFieldValue(Object target, String fieldName, Object value)
            throws IllegalArgumentException, IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("Target object cannot be null");
        }

        String cacheKey = target.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            Field f = findField(target.getClass(), fieldName);
            if (f == null) {
                throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy of " + target.getClass());
            }
            f.setAccessible(true); // 一次性绕过访问控制
            return f;
        });

        // 设置值（自动处理基本类型装箱）
        field.set(target, value);
    }

    // 可选：提供强类型版本（避免运行时类型错误）
    public static void setIntField(Object target, String fieldName, int value) throws Exception {
        getField(target, fieldName).setInt(target, value);
    }

    public static void setLongField(Object target, String fieldName, long value) throws Exception {
        getField(target, fieldName).setLong(target, value);
    }

    // 辅助方法：获取已缓存的字段（用于 get 或强类型 set）
    private static Field getField(Object target, String fieldName) throws Exception {
        String cacheKey = target.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.get(cacheKey);
        if (field == null) {
            field = findField(target.getClass(), fieldName);
            if (field == null) {
                throw new NoSuchFieldException(fieldName);
            }
            field.setAccessible(true);
            FIELD_CACHE.putIfAbsent(cacheKey, field);
        }
        return field;
    }

    static class Animal {
        private String name;
    }

    static class Dog extends Animal {
        private int age;
    }

    static void main() throws Exception {
        Dog dog = new Dog();

        // 设置父类字段
        ReflectionFieldSetter.setFieldValue(dog, "name", "Buddy");
        // 设置子类字段
        ReflectionFieldSetter.setFieldValue(dog, "age", 3);

        // 验证（可用类似方式读取）
        System.out.println("Name: " + getField(dog, "name")); // Buddy
        System.out.println("Age: " + getField(dog, "age"));   // 3
    }

}
