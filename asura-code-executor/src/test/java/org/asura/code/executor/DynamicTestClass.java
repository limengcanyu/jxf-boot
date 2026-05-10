package org.asura.code.executor;

import java.util.Arrays;

public class DynamicTestClass {
    // main方法
    public static void main(String[] args) {
        System.out.println("执行main方法: " + Arrays.toString(args));
    }

    // 静态带返回值方法
    public static int staticMethodWithReturn(int a, int b) {
        return a + b;
    }

    // 静态不带返回值方法
    public static void staticMethodWithoutReturn(String message) {
        System.out.println("静态无返回值方法接收参数: " + message);
    }

    // 非静态带返回值方法
    public String instanceMethodWithReturn(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }

    // 非静态不带返回值方法
    public void instanceMethodWithoutReturn(int count) {
        System.out.println("非静态无返回值方法 - 计数: " + count);
    }
}
