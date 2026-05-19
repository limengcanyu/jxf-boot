package org.asura.postgressql;

import com.alibaba.fastjson.JSONObject;
import org.asura.postgressql.dao.entity.Company;
import org.asura.postgressql.service.ICompanyService;
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
