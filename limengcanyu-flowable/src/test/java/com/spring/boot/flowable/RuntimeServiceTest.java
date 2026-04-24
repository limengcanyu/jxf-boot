package com.spring.boot.flowable;

import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: </p>
 *
 * 与提供静态信息（也就是不会改变，至少不会经常改变的信息）的RepositoryService相反，RuntimeService用于启动流程定义的新流程实例。
 * 前面介绍过，流程定义中定义了流程中不同步骤的结构与行为。流程实例则是流程定义的实际执行过程。同一时刻，一个流程定义通常有多个运行中的实例。
 * RuntimeService也用于读取与存储流程变量。流程变量是流程实例中的数据，可以在流程的许多地方使用（例如排他网关经常使用流程变量判断流程下一步要走的路径）。
 * RuntimeService还可以用于查询流程实例与执行(Execution)。执行也就是BPMN 2.0中 'token' 的概念。通常执行是指向流程实例当前位置的指针。
 * 最后，还可以在流程实例等待外部触发时使用RuntimeService，使流程可以继续运行。
 * 流程有许多等待状态(wait states)，RuntimeService服务提供了许多操作用于“通知”流程实例：已经接收到外部触发，流程实例可以继续运行。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:25
 */
@SpringBootTest
public class RuntimeServiceTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey = "test-process2";
        runtimeService.startProcessInstanceByKey(processDefinitionKey);
    }
}
