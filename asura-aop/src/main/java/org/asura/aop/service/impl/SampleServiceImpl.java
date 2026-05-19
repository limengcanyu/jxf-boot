package org.asura.aop.service.impl;

import org.asura.aop.service.SampleService;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {
    @Override
    public String compute() {
        return "compute successfully";
    }
}
