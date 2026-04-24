package com.spring.boot.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TestBatchService testBatchService;

    @GetMapping("/queryById/{id}")
    public Integer queryById(@PathVariable Integer id) {
        return testService.queryById(id);
    }

    @GetMapping("/mergeQueryById/{id}")
    public Integer mergeQueryById(@PathVariable Integer id) throws ExecutionException, InterruptedException {
        Future<Integer> result = testBatchService.queryIntegerById(id);
        return result.get();
    }
}
