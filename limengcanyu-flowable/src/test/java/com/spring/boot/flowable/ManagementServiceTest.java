package com.spring.boot.flowable;

import org.flowable.engine.ManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Description: </p>
 *
 * ManagementService通常在用Flowable编写用户应用时不需要使用。它可以读取数据库表与表原始数据的信息，也提供了对作业(job)的查询与管理操作。
 * Flowable中很多地方都使用作业，例如定时器(timer)，异步操作(asynchronous continuation)，延时暂停/激活(delayed suspension/activation)等等。
 * 后续会详细介绍这些内容。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:34
 */
@SpringBootTest
public class ManagementServiceTest {

    @Autowired
    private ManagementService managementService;

    @Test
    public void test() {
    }
}
