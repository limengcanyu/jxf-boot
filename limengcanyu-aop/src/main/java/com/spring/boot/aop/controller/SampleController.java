package com.spring.boot.aop.controller;

import com.spring.boot.aop.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    /**
     * localhost:8080/compute?tenantId=tenant_001&companyId=company_001&salaryMonth=2020-01
     *
     * @return
     */
    @RequestMapping("/compute")
    public String compute(@RequestParam String tenantId, @RequestParam String companyId, @RequestParam String salaryMonth) throws Exception {
//        return sampleService.compute();
        throw new Exception();
    }
}
