package com.spring.boot.hystrix.demo;

import com.netflix.hystrix.examples.demo.GetUserAccountCommand;
import com.netflix.hystrix.examples.demo.UserAccount;

import java.net.HttpCookie;

/**
 * POJO 
 */
public class Order {

    private final int orderId;
    private UserAccount user;

    public Order(int orderId) {
        this.orderId = orderId;

        /* a contrived example of calling GetUserAccount again */
        user = new GetUserAccountCommand(new HttpCookie("mockKey", "mockValueFromHttpRequest")).execute();
    }

}
