package com.spring.boot.mybatisplus.druid.dynamic.datasource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.entity.SysUser;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.mapper.SysUserMapper;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author rock
 * @since 2019-05-19
 */
//@DynamicSwitchDataSource(dataSource = "datasource")
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

//    @DynamicSwitchDataSource(dataSource = "artanis")
//    @Override
//    public SysUser selectSysUserFromArtanis(Integer id) {
//        return getById(id);
//    }
//
//    @DynamicSwitchDataSource(dataSource = "alita")
//    @Override
//    public SysUser selectSysUserFromAlita(Integer id) {
//        return getById(id);
//    }
}
