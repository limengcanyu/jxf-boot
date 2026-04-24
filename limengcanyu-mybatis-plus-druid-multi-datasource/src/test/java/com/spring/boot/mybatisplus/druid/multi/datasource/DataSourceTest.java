//package com.spring.boot.mybatisplus.druid.multidatasource;
//
//import com.spring.boot.mybatisplus.druid.multidatasource.artanis.dao.entity.SysUser;
//import com.spring.boot.mybatisplus.druid.multidatasource.artanis.service.SysUserService;
//import com.spring.boot.mybatisplus.druid.multidatasource.gt1.dao.entity.SalaryGrantEmployee;
//import com.spring.boot.mybatisplus.druid.multidatasource.gt1.service.SalaryGrantEmployeeService;
//import com.spring.boot.mybatisplus.druid.multidatasource.samuro.dao.entity.SysCompany;
//import com.spring.boot.mybatisplus.druid.multidatasource.samuro.service.SysCompanyService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * <p>Description: </p>
// *
// * @author Rock Jiang
// * @version 1.0
// * @date 2018/5/23 0023
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DruidMultiDatasourceApplication.class)
//public class DataSourceTest {
//    @Autowired
//    SysUserService sysUserService;
//    @Autowired
//    SysCompanyService sysCompanyService;
//    @Autowired
//    SalaryGrantEmployeeService salaryGrantEmployeeService;
//
//    @Test
//    public void test() {
//        SysUser sysUser = sysUserService.selectById(3);
//        System.out.println("sysUser: " + sysUser);
//
//        SysCompany sysCompany = sysCompanyService.selectById(1);
//        System.out.println("sysCompany: " + sysCompany);
//
//        SalaryGrantEmployee salaryGrantEmployee = salaryGrantEmployeeService.selectById(1);
//        System.out.println("salaryGrantEmployee: " + salaryGrantEmployee);
//
//    }
//}
