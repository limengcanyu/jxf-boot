package com.spring.boot.tomcat.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Cacheable("piDecimals")
    public int computePiDecimal(int precision) {
        System.out.println("======> calling computePiDecimal");
        return 1;
    }

}
