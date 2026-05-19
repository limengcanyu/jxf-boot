package org.asura.shardingsphere;

import org.asura.shardingsphere.entity.Address;
import org.asura.shardingsphere.service.IAddressService;
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
