package com.spring.boot.mybatis.plus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spring.boot.mybatis.plus.entity.User;
import com.spring.boot.mybatis.plus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rock
 * @since 2022-12-29
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * http://localhost:8081/user/save
     *
     * @return
     */
    @RequestMapping("/save")
    public User save() {
        User user = new User();
        user.setId(6L);
        user.setName("rock");
        user.setAge(21);
        user.setEmail("rock@163.com");
        userService.save(user);

        return user;
    }

    /**
     * http://localhost:8081/user/update
     *
     * @return
     */
    @RequestMapping("/update")
    public User update() {
        User user = new User();
        user.setAge(25);
        user.setName("rock");

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(User::getId, 6L);
        userService.update(user, updateWrapper);

        return userService.getById(6L);
    }

    /**
     * http://localhost:8081/user/query
     *
     * @return
     */
    @RequestMapping("/query")
    public User query() {
        return userService.getById(6L);
    }

    /**
     * http://localhost:8081/user/delete
     *
     * @return
     */
    @RequestMapping("/delete")
    public User delete() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, 6L);
        userService.remove(queryWrapper);

        return userService.getById(6L);
    }

}
