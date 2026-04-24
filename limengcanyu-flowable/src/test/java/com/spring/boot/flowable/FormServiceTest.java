package com.spring.boot.flowable;

import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: </p>
 *
 * FormService是可选服务。也就是说Flowable没有它也能很好地运行，而不必牺牲任何功能。这个服务引入了开始表单(start form)与任务表单(task form)的概念。
 * 开始表单是在流程实例启动前显示的表单，而任务表单是用户完成任务时显示的表单。Flowable可以在BPMN 2.0流程定义中定义这些表单。
 * 表单服务通过简单的方式暴露这些数据。再次重申，表单不一定要嵌入流程定义，因此这个服务是可选的。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:34
 */
@SpringBootTest
public class FormServiceTest {

    @Autowired
    private FormService formService;

    @Test
    public void test() {
    }
}
