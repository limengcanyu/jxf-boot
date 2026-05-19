package org.asura.undertow;

import org.asura.undertow.service.ProjectConfigurationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: RedisUtils Test</p>
 *
 * @author rock.jiang
 * date 2019/06/20
 */
@SpringBootTest
class AsuraUndertowApplicationTests {
    @Autowired
    private ProjectConfigurationService projectConfigurationService;

    @Test
    void getCurrentCompanyName() {
        System.out.println(projectConfigurationService.getCurrentCompanyName());
    }

}
