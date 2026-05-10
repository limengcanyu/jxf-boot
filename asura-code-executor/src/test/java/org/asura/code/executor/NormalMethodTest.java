package org.asura.code.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalMethodTest {
    private static final Logger log = LoggerFactory.getLogger(NormalMethodTest.class);

    public static Integer calculate(int a, int b) {
        System.out.println("执行calculate方法，参数：a=" + a + ", b=" + b);
        log.info("计算：{}+{}", a, b);
        return a + b;
    }
}
