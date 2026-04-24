package com.spring.boot.shardingsphere.encrypt.algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.context.EncryptContext;

@Getter
@Setter
public final class IntegerEncryptAlgorithm implements EncryptAlgorithm<Object, Integer> {

    @Override
    public void init() {

    }

    @Override
    public Integer encrypt(Object plainValue, EncryptContext encryptContext) {
        if (plainValue instanceof Integer) {
            return (Integer) plainValue * 25 + 2022;
        }

        return null;
    }

    @Override
    public Object decrypt(Integer cipherValue, EncryptContext encryptContext) {
        return (cipherValue - 2022) / 25;
    }


    @Override
    public String getType() {
        return "INTEGER";
    }
}
