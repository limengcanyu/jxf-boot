package com.spring.boot.shardingsphere.service.impl;

import com.spring.boot.shardingsphere.entity.Address;
import com.spring.boot.shardingsphere.mapper.AddressMapper;
import com.spring.boot.shardingsphere.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock
 * @since 2022-06-16
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
