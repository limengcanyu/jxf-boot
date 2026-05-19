package org.asura.skywalking.service;

import com.spring.boot.mybatis.plus.entity.User;
import com.spring.boot.mybatis.plus.service.UserService;

import java.util.List;

public interface MyUserService extends UserService {

    /**
     * 异步链路追踪
     *
     * @return
     */
    List<User> asyncGetUser();

}
