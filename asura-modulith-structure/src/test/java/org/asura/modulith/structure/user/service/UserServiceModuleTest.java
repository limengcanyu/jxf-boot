package org.asura.modulith.structure.user.service;

import jakarta.annotation.Resource;
import org.asura.modulith.structure.order.api.OrderApiService;
import org.asura.modulith.structure.shared.event.user.UserPlaceOrderEvent;
import org.asura.modulith.structure.user.dto.CreateUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ApplicationModuleTest
public class UserServiceModuleTest {

    @Resource
    private UserService userService;

    /**
     * 模拟订单服务，用于测试异步下单
     */
    @MockitoBean
    private OrderApiService orderApiService;

    @Test
    void asyncPlaceOrder(Scenario scenario) {
        // 执行异步下单操作
        userService.asyncPlaceOrder(1L, 2);

        // 验证事件是否被发布
        scenario.publish(new UserPlaceOrderEvent(1L, 2))
                .andWaitForEventOfType(UserPlaceOrderEvent.class)
                .matching(event -> event.userId() == 1L && event.goodsNum() == 2)
                .toArrive();
    }

    @Test
    void syncCreateOrder() {
        // 测试同步下单
        Long orderId = userService.syncCreateOrder(1L, 2);
        assert orderId != null;
    }

    @Test
    void createUser() {
        // 测试创建用户
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUserId(100L);
        createUserDTO.setUserName("Test User");
        userService.createUser(createUserDTO);
    }

}
