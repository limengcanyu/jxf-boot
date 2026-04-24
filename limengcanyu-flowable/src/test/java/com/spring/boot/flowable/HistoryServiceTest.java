package com.spring.boot.flowable;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * HistoryService暴露Flowable引擎收集的所有历史数据。当执行流程时，引擎会保存许多数据（可配置），
 * 例如流程实例启动时间、谁在执行哪个任务、完成任务花费的事件、每个流程实例的执行路径，等等。这个服务主要提供查询这些数据的能力。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:34
 */
@SpringBootTest
public class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;

    @Test
    public void createHistoricActivityInstanceQuery() {
        List<HistoricActivityInstance> list =  historyService.createHistoricActivityInstanceQuery().finished().list();
        for (HistoricActivityInstance instance : list) {
            System.out.println(instance);
        }
    }
}
