package org.asura.modulith.structure.user.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceBootTest {

    @Resource
    private UserService userService;

    @Test
    void asyncPlaceOrder() {
        userService.asyncPlaceOrder(1L, 2);
    }

}
