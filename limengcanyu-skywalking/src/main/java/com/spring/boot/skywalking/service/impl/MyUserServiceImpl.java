package com.spring.boot.skywalking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.boot.mybatis.plus.entity.User;
import com.spring.boot.mybatis.plus.service.impl.UserServiceImpl;
import com.spring.boot.skywalking.service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class MyUserServiceImpl extends UserServiceImpl implements MyUserService {

    @Autowired
    private Executor asyncTaskExecutor;

    @Override
    public List<User> asyncGetUser() {
        // 异步有结果返回
        CompletableFuture<List<User>> future1 = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getName, "Jone");
            return list(queryWrapper);
        }), asyncTaskExecutor);

        CompletableFuture<List<User>> future2 = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getName, "Jack");
            return list(queryWrapper);
        }), asyncTaskExecutor);

        // 异步无结果返回
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(new RunnableWrapper(() -> {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getName, "Tom");
            List<User> userList3 = list(queryWrapper);
            log.debug("userList3: {}", userList3);
        }), asyncTaskExecutor);

        CompletableFuture.allOf(future1, future2, future3).join();

        try {
            List<User> userList1 = future1.get(6, TimeUnit.SECONDS);
            List<User> userList2 = future2.get(6, TimeUnit.SECONDS);

            log.debug("userList1: {}", userList1);
            log.debug("userList2: {}", userList2);

            userList1.addAll(userList2);

            return userList1;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
