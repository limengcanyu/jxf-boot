package org.asura.skywalking.service;

import org.asura.skywalking.vo.User;

import java.util.List;

public interface MyUserService {

    /**
     * 异步链路追踪
     *
     * @return
     */
    List<User> asyncGetUser();

}
