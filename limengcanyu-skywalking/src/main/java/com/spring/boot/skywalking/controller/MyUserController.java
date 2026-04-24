package com.spring.boot.skywalking.controller;

import com.spring.boot.mybatis.plus.controller.UserController;
import com.spring.boot.mybatis.plus.entity.User;
import com.spring.boot.mybatis.plus.service.UserService;
import com.spring.boot.skywalking.service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class MyUserController extends UserController {

    @Autowired
    private MyUserService myUserService;

    /**
     * http://localhost:8081/user/asyncGetUser
     *
     * @return
     */
    @RequestMapping("/asyncGetUser")
    public List<User> asyncGetUser() {
        log.debug("call asyncGetUser =====================");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return myUserService.asyncGetUser();
    }

}
