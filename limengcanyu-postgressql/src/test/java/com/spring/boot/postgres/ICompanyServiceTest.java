package com.spring.boot.postgres;

import com.alibaba.fastjson.JSONObject;
import com.spring.boot.postgres.dao.entity.Company;
import com.spring.boot.postgres.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ICompanyServiceTest {
    @Autowired
    private ICompanyService companyService;

    @Test
    public void test() {
        Company company = companyService.getById(1);
        log.debug("company: {}", JSONObject.toJSONString(company));
    }
}
