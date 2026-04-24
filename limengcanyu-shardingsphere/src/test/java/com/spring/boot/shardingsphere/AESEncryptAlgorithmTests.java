package com.spring.boot.shardingsphere;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shardingsphere.encrypt.algorithm.AESEncryptAlgorithm;
import org.apache.shardingsphere.encrypt.algorithm.MD5EncryptAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AESEncryptAlgorithmTests {

    @Test
    public void aes() {
        AESEncryptAlgorithm aesEncryptAlgorithm = new AESEncryptAlgorithm();
        aesEncryptAlgorithm.setSecretKey(Arrays.copyOf(DigestUtils.sha1("123456abc"), 16));
        String encrypt = aesEncryptAlgorithm.encrypt("13852331504", null);
        System.out.println("encrypt: " + encrypt);
        Object decrypt = aesEncryptAlgorithm.decrypt(encrypt, null);
        System.out.println("decrypt: " + decrypt);
    }

    @Test
    public void md5() {
        MD5EncryptAlgorithm md5EncryptAlgorithm = new MD5EncryptAlgorithm();
        String encrypt = md5EncryptAlgorithm.encrypt("abc1004", null);
        System.out.println("encrypt: " + encrypt);
        Object decrypt = md5EncryptAlgorithm.decrypt(encrypt, null);
        System.out.println("decrypt: " + decrypt);
    }

}
