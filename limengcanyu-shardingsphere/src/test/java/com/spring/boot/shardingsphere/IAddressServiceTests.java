package com.spring.boot.shardingsphere;

import com.spring.boot.shardingsphere.entity.Address;
import com.spring.boot.shardingsphere.service.IAddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class IAddressServiceTests {

    @Autowired
    private IAddressService iAddressService;

    @Test
    public void save() {
        Address address = new Address();
        address.setCreateTime(LocalDateTime.now());
        iAddressService.save(address);
    }
}
