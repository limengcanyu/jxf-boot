package com.spring.boot.shardingsphere.encrypt.algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.context.EncryptContext;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public final class DateEncryptAlgorithm implements EncryptAlgorithm<Object, Date> {

    @Override
    public void init() {

    }

    @Override
    public Date encrypt(Object plainValue, EncryptContext encryptContext) {
        if (plainValue instanceof LocalDate value) {
            return Date.valueOf(value.minusYears(30).plusMonths(5).plusDays(21));
        }

        return null;
    }

    @Override
    public Object decrypt(Date cipherValue, EncryptContext encryptContext) {
        return cipherValue.toLocalDate().plusYears(30).minusMonths(5).minusDays(21);
    }

    @Override
    public String getType() {
        return "DATE";
    }

}
