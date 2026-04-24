package com.spring.boot.multi.datasource.transaction.service.impl;

import com.spring.boot.multi.datasource.transaction.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>Description: </p>
 *
 * @author rock.jxf
 * @date 2020/12/5 0:40
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Document insertEmployee() {
        Document employee = new Document();
        employee.put("tenant_id", "tenant_000001");
        employee.put("company_id", "company_000001");
        employee.put("salary_month", "2019-01");
        employee.put("employee_id", "employee_000001");
        employee.put("employee_name", "王一");
        employee.put("department_id", "department_000001");
        employee.put("department_name", "部门000001");

        Document iDoc = mongoTemplate.insert(employee, "artanis");
        log.debug("iDoc: {}", iDoc);

        return iDoc;
    }
}
