package org.asura.skywalking.controller;

import lombok.extern.slf4j.Slf4j;
import org.asura.skywalking.service.MyUserService;
import org.asura.skywalking.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class MyUserController {

    @Autowired
    private MyUserService myUserService;

    /**
     * <a href="http://localhost:8080/user/asyncGetUser">...</a>
     *
     */
    @GetMapping("/asyncGetUser")
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
