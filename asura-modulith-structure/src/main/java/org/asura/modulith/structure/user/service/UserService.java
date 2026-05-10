package org.asura.modulith.structure.user.service;

import org.asura.modulith.structure.user.dto.CreateUserDTO;

public interface UserService {

    // 同步下单
    Long syncCreateOrder(Long userId, Integer goodsNum);

    // 事件异步下单
    void asyncPlaceOrder(Long userId, Integer goodsNum);

    void createUser(CreateUserDTO createUserDTO);

}
