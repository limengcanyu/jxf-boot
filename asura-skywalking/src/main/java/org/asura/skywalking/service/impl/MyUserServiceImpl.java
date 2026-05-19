package org.asura.skywalking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;
import org.asura.skywalking.service.MyUserService;
import org.asura.skywalking.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class MyUserServiceImpl implements MyUserService {

    @Autowired
    private Executor asyncTaskExecutor;

    @Override
    public List<User> asyncGetUser() {
        // 异步有结果返回
        CompletableFuture<List<User>> future1 = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> {
            return List.of(new User(1L, "asura"));
        }), asyncTaskExecutor);

        CompletableFuture<List<User>> future2 = CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> {
            return List.of(new User(2L, "artanis"));
        }), asyncTaskExecutor);

        // 异步无结果返回
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(new RunnableWrapper(() -> {
            List<User> userList3 = List.of(new User(3L, "akuma"));
            log.debug("userList3: {}", userList3);
        }), asyncTaskExecutor);

        CompletableFuture.allOf(future1, future2, future3).join();

        try {
            List<User> userList1 = new ArrayList<>(future1.get(6, TimeUnit.SECONDS));
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