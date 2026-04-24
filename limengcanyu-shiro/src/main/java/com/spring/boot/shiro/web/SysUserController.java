package com.spring.boot.shiro.web;//package com.spring.boot.shiro.web;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.spring.boot.shiro.dao.entity.SysUser;
//import com.spring.boot.shiro.service.SysUserService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authz.annotation.RequiresRoles;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Created by Administrator on 2018/1/15.
// */
//@RestController
//@RequestMapping("/sysUser")
//public class SysUserController {
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    /**
//     * AR 部分测试
//     */
//    @GetMapping("/test1")
//    @RequiresRoles("admin")
////    @RequiresPermissions("test1")
//    public Page<SysUser> test1() {
//        System.out.println("SysUserController test1");
////        Boolean checkResult = SecurityUtils.getSubject().isPermitted("admin");
////        try {
////            sysUserService.myTest();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        return null;
//    }
//
//    @GetMapping("/test2")
////    @RequiresRoles("admin")
//    public Object test2() {
//        System.out.println("SysUserController test2");
//        Subject subject = SecurityUtils.getSubject();
//        System.out.println("Principal: " + subject.getPrincipal());
////        try {
////            return sysUserService.myTest2();
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
//        return null;
//    }
//
//}
