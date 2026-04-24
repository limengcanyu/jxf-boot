package com.spring.boot.mybatis.plus.service.impl;

import com.spring.boot.mybatis.plus.entity.Address;
import com.spring.boot.mybatis.plus.mapper.AddressMapper;
import com.spring.boot.mybatis.plus.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock
 * @since 2022-12-10
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
