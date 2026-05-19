package org.asura.shardingsphere.service.impl;

import org.asura.shardingsphere.entity.Address;
import org.asura.shardingsphere.mapper.AddressMapper;
import org.asura.shardingsphere.service.IAddressService;
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
