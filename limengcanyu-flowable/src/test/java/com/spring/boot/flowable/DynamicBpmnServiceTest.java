package com.spring.boot.flowable;

import org.flowable.engine.DynamicBpmnService;
import org.flowable.engine.FormService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: </p>
 *
 * DynamicBpmnService可用于修改流程定义中的部分内容，而不需要重新部署它。例如可以修改流程定义中一个用户任务的办理人设置，或者修改一个服务任务中的类名。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:34
 */
@SpringBootTest
public class DynamicBpmnServiceTest {

    @Autowired
    private DynamicBpmnService dynamicBpmnService;

    @Test
    public void test() {
    }
}
