package org.asura.code.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMethodTest {
    private static final Logger logger = LoggerFactory.getLogger(MainMethodTest.class.getName());

    public static void main(String[] args) {
        System.out.println("=== Main方法执行开始 ===");
        System.err.println("这是错误流输出");
        logger.info("Main方法参数个数：{}", args.length);
        for (int i = 0; i < args.length; i++) {
            logger.warn("参数{}：{}", i, args[i]);
        }
        System.out.println("=== Main方法执行结束 ===");
    }

    public static Integer sum1(int a, int b) {
        logger.info("sum1执行结果：{}", a + b);
        return a + b;
    }

    public static void sum2(int a, int b) {
        System.out.println("sum2执行结果：" + (a + b));
    }

    public Integer sum3(int a, int b) {
        logger.info("sum3执行结果：{}", a + b);
        return a + b;
    }

    public void sum4(int a, int b) {
        System.out.println("sum4执行结果：" + (a + b));
    }

}
