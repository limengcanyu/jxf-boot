package com.spring.boot.mybatisplus.druid.dynamic.datasource.controller;

import com.spring.boot.mybatisplus.druid.dynamic.datasource.entity.SysUser;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("/hello")
    public String hello() {
        System.out.println("HelloController hello");

        return "hello";
    }

    @RequestMapping("/alita")
    public String alita() {
        System.out.println("HelloController alita");

        SysUser sysUser = sysUserService.getById(1);
        System.out.println(sysUser);

        return "hello";
    }
}
