package com.spring.boot.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    public List<Integer> queryByBatch(List<Integer> ids){
        System.out.println("service queryByBatch ============================");

        List<Integer> retList = new ArrayList<>();

        return retList;
    }

    public Integer queryById(Integer id){
        System.out.println("service queryById ============================");

        return ++id;
    }
}
