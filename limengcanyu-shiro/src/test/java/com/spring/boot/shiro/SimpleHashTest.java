package com.spring.boot.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/3/30 0030
 */
public class SimpleHashTest {
    @Test
    public void hashTest() {
        //算法名称 MD5
        String hashAlgorithmName = "MD5";
        String username = "rock";
        //密码
        String credentials = "123456";
        //hash次数
        int hashIterations = 1024;
        ByteSource credentialsSalt = ByteSource.Util.bytes("rock123456");
        SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, "123456", "cm9jazEyMzQ1Ng==", hashIterations);
        System.out.println("simpleHash: " + simpleHash);

        Md5Hash md5Hash = new Md5Hash(username, credentials, 1024);
        System.out.println("md5Hash: " + md5Hash);
    }
}
