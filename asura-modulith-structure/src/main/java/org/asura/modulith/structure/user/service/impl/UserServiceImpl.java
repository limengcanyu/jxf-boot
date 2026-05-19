package org.asura.modulith.structure.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.order.api.OrderApiService;
import org.asura.modulith.structure.shared.event.user.UserPlaceOrderEvent;
import org.asura.modulith.structure.user.dto.CreateUserDTO;
import org.asura.modulith.structure.user.entity.User;
import org.asura.modulith.structure.user.mapper.UserMapper;
import org.asura.modulith.structure.user.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // 同步：仅依赖订单api抽象接口
    private final OrderApiService orderApiService;
    // 异步：事件发布器
    private final ApplicationEventPublisher eventPublisher;
    private final UserMapper userMapper;

    // 同步下单
    public Long syncCreateOrder(Long userId, Integer goodsNum){
        checkUser(userId);
        return orderApiService.createOrder(userId, goodsNum);
    }

    // 事件异步下单
    public void asyncPlaceOrder(Long userId, Integer goodsNum){
        checkUser(userId);
        eventPublisher.publishEvent(new UserPlaceOrderEvent(userId, goodsNum));
        System.out.println("User 异步下单事件已发布");
    }

    private void checkUser(Long userId){
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
    }

    public void createUser(CreateUserDTO createUserDTO) {
        User user = new User(createUserDTO.getUserId(), createUserDTO.getUserName());
        userMapper.insert(user);
    }

}
