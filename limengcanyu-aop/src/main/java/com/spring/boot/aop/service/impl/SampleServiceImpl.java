package com.spring.boot.aop.service.impl;

import com.spring.boot.aop.service.SampleService;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {
    @Override
    public String compute() {
        return "compute successfully";
    }
}
