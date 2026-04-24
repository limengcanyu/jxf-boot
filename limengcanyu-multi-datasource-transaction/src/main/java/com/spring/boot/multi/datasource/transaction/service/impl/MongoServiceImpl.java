package com.spring.boot.multi.datasource.transaction.service.impl;

import com.spring.boot.multi.datasource.transaction.service.EmployeeService;
import com.spring.boot.multi.datasource.transaction.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoServiceImpl implements MongoService {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public String saveMongoRecord() {
        employeeService.insertEmployee();
        return "success";
    }
}
