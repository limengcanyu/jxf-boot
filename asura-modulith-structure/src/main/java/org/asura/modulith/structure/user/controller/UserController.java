package org.asura.modulith.structure.user.controller;

import lombok.RequiredArgsConstructor;
import org.asura.modulith.structure.shared.result.R;
import org.asura.modulith.structure.user.dto.CreateUserDTO;
import org.asura.modulith.structure.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 同步调用订单模块API
    @GetMapping("/sync/order")
    public R<Long> syncOrder(@RequestParam Long userId, @RequestParam Integer goodsNum){
        return R.ok(userService.syncCreateOrder(userId, goodsNum));
    }

    // 事件驱动异步下单
    @GetMapping("/async/order")
    public R<Void> asyncOrder(@RequestParam Long userId, @RequestParam Integer goodsNum){
        userService.asyncPlaceOrder(userId, goodsNum);
        return R.ok();
    }

    @GetMapping("/create")
    public R<Boolean> createUser(@RequestBody CreateUserDTO createUserDTO){
        userService.createUser(createUserDTO);
        return R.ok(true);
    }

}
