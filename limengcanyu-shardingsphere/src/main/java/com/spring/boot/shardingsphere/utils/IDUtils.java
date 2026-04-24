package com.spring.boot.shardingsphere.utils;

import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;

public class IDUtils {

    private final static SnowflakeKeyGenerateAlgorithm algorithm = new SnowflakeKeyGenerateAlgorithm();

    public static Long generateKey() {
        Comparable<?> comparable = algorithm.generateKey();
        if (comparable instanceof Long aLong) {
            return aLong;
        }

        return null;
    }
}
