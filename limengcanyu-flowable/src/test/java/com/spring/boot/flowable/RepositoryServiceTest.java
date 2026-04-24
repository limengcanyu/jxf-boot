package com.spring.boot.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * RepositoryService很可能是使用Flowable引擎要用的第一个服务。这个服务提供了管理与控制部署(deployments)与流程定义(process definitions)的操作。在这里简单说明一下，流程定义是BPMN 2.0流程对应的Java对象，体现流程中每一步的结构与行为。部署是Flowable引擎中的包装单元，一个部署中可以包含多个BPMN 2.0 XML文件及其他资源。开发者可以决定在一个部署中包含的内容，可以是单个流程的BPMN 2.0 XML文件，也可以包含多个流程及其相关资源（如’hr-processes’部署可以包含所有与人力资源流程相关的的东西）。RepositoryService可用于部署这样的包。部署意味着将它上传至引擎，引擎将在储存至数据库之前检查与分析所有的流程。在部署操作后，可以在系统中使用这个部署包，部署包中的所有流程都可以启动。
 *
 * 此外，这个服务还可以：
 *
 * 查询引擎现有的部署与流程定义。
 *
 * 暂停或激活部署中的某些流程，或整个部署。暂停意味着不能再对它进行操作，激活刚好相反，重新使它可以操作。
 *
 * 获取各种资源，比如部署中保存的文件，或者引擎自动生成的流程图。
 *
 * 获取POJO版本的流程定义。它可以用Java而不是XML的方式查看流程。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:27
 */
@SpringBootTest
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void createProcessDefinitionQuery() {
        System.out.println("all-------------------------------------------");
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition definition : list) {
            System.out.println(definition);
        }

        System.out.println("active-------------------------------------------");
        list = repositoryService.createProcessDefinitionQuery().active().list();
        for (ProcessDefinition definition : list) {
            System.out.println(definition);
        }

        System.out.println("startableByUser-------------------------------------------");
        list = repositoryService.createProcessDefinitionQuery().startableByUser("user001").list();
        for (ProcessDefinition definition : list) {
            System.out.println(definition);
        }
    }
}
