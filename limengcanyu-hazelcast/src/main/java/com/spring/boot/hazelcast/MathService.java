package com.spring.boot.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MathService {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public int computePiDecimal(int i) {
        // ...
        int result = i + 1;
        hazelcastInstance.getMap("piDecimals").put(1, result);
        return result;
    }

}
