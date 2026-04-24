package com.spring.boot.shardingsphere;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.boot.shardingsphere.entity.User;
import com.spring.boot.shardingsphere.mapper.UserMapper;
import com.spring.boot.shardingsphere.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class IUserServiceTests {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void save() {
        for (int i = 1; i <= 3; i++) {
            User user = new User();
            user.setUserId((long) i);
//            user.setFullname("张三" + i);
//            user.setAge(20 + i);
////            user.setBirthday(LocalDate.parse("2007-12-0" + i));
//            user.setPwd("abc100" + i);
//            user.setMobile("1385233150" + i);
//            user.setIdCard("32082919870801220" + i);
            iUserService.save(user);
        }
    }

    @Test
    public void insertNew() {
        User user = new User();
        user.setUserId((long) 4);
//        user.setFullname("张三" + 4);
//        user.setAge(20 + 4);
//        user.setBirthday(LocalDate.parse("2007-12-0" + 4));
//        user.setPwd("abc100" + 4);
//        user.setMobile("1385233150" + 4);
//        user.setIdCard("32082919870801220" + 4);
//        userMapper.insertNew(user);
        user.setCreateTime(LocalDateTime.now());
        iUserService.save(user);
    }

    @Test
    public void select() {
//        List<User> list = iUserService.list();

//        List<User> list = userMapper.selectAll();
//        list.forEach(System.out::println);

//        // 使用加密列查询
//        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        userLambdaQueryWrapper.eq(User::getIdCard, "320829198708012201");
//        List<User> list1 = iUserService.list(userLambdaQueryWrapper);
//        list1.forEach(System.out::println);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserId, 1);
        List<User> list1 = iUserService.list(userLambdaQueryWrapper);
        list1.forEach(System.out::println);
    }

    @Test
    public void readwriteSplitting() {
        // 测试写
        User user = new User();
        user.setUserId((long) 4);
        user.setCreateTime(LocalDateTime.now());
        iUserService.save(user);

        // 测试读
        for (int i = 0; i < 2; i++) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getUserId, 1);
            List<User> list1 = iUserService.list(userLambdaQueryWrapper);
            list1.forEach(System.out::println);
        }
    }
}
