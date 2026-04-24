package com.spring.boot.netty.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class InetAddressUtils {

    public static String getLocalHost() {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
//            log.debug("获取本机IP: {}", host);
        } catch (UnknownHostException e) {
            log.debug("获取本机IP异常");
            throw new RuntimeException(e);
        }

        return "127.0.0.1";
    }
}
