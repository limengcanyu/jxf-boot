package com.spring.boot.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class TestBatchService {

    /**
     * 该方法体不会执行，因此返回null
     *
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod = "queryIntegerBatchById", scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            collapserProperties = {
                    @HystrixProperty(name = "maxRequestsInBatch", value = "10") // 每10个请求合并1次
            })
    public Future<Integer> queryIntegerById(Integer id) {
        System.out.println("service queryById ============================");

        return null;
    }

    @HystrixCommand
    public List<Integer> queryIntegerBatchById(List<Integer> ids) {
        System.out.println("service queryByBatch ============================");

        List<Integer> retList = new ArrayList<>();

        for (Integer id : ids) {
            retList.add(++id);
        }

        return retList;
    }

}
