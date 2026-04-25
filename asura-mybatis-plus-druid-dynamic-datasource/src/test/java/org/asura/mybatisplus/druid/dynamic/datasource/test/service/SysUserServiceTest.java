package org.asura.mybatisplus.druid.dynamic.datasource.test.service;

import org.asura.mybatisplus.druid.dynamic.datasource.entity.SysUser;
import org.asura.mybatisplus.druid.dynamic.datasource.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SysUserServiceTest {
    @Autowired
    private SysUserService sysUserService;

    @Test
    public void test() {
        SysUser sysUser = sysUserService.getById(1);
        System.out.println(sysUser);

    }

}
