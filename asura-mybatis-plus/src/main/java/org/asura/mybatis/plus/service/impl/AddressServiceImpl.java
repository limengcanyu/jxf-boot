package org.asura.mybatis.plus.service.impl;

import org.asura.mybatis.plus.entity.Address;
import org.asura.mybatis.plus.mapper.AddressMapper;
import org.asura.mybatis.plus.service.AddressService;
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
