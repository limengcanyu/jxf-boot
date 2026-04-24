package com.spring.boot.multi.datasource.transaction.service.impl;

import com.spring.boot.multi.datasource.transaction.annotation.MultiTransactional;
import com.spring.boot.multi.datasource.transaction.dao.entity.PlatformSalaryItem;
import com.spring.boot.multi.datasource.transaction.service.AggregateService;
import com.spring.boot.multi.datasource.transaction.service.IPlatformSalaryItemService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jxf
 * @date 2020/12/5 0:47
 */
@Slf4j
@Service
public class AggregateServiceImpl implements AggregateService {

    @Autowired
    private IPlatformSalaryItemService platformSalaryItemService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MultiTransactional(transactionManager = {"dataSourceTransactionManager", "mongoTransactionManager"})
    @Override
    public String aggregate() throws Exception {
        // put your business logic here
        PlatformSalaryItem item = new PlatformSalaryItem();
        item.setItemCode("item_134");
        item.setItemName("item_134");
        platformSalaryItemService.save(item);

        Document employee = new Document();
        employee.put("tenant_id", "tenant_000001");
        employee.put("company_id", "company_000001");
        employee.put("salary_month", "2019-01");
        employee.put("employee_id", "employee_000001");
        employee.put("employee_name", "王一");
        employee.put("department_id", "department_000001");
        employee.put("department_name", "部门000001");

        Document iDoc = mongoTemplate.insert(employee, "employee");
        log.debug("iDoc: {}", iDoc);

        throw new Exception();
//        return "success";
    }

    @MultiTransactional
    @Override
    public String saveItemData() throws Exception {
        // mysql保存数据结构
        List<PlatformSalaryItem> itemList = new ArrayList<>();

        PlatformSalaryItem item = new PlatformSalaryItem();
        item.setItemCode("item_001");
        item.setItemName("薪资项001");
        itemList.add(item);

        item = new PlatformSalaryItem();
        item.setItemCode("item_002");
        item.setItemName("薪资项002");
        itemList.add(item);
        platformSalaryItemService.saveBatch(itemList);

        // mongo保存数据
        Document record = new Document();
        record.put("tenant_id", "tenant_001");
        record.put("company_id", "company_001");
        record.put("month", "2020-12");
        record.put("item_001", 200.00); // 数据结构项赋值
        record.put("item_002", 300.00); // 数据结构项赋值
        record = mongoTemplate.insert(record, "item_record");
        log.debug("record: {}", record);

        return "success";
    }
}
