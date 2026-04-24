package com.spring.boot.shiro.web;//package com.spring.boot.shiro.web;
//
//import com.alibaba.fastjson.JSONObject;
//import com.spring.boot.shiro.dao.entity.UserInfo;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.LockedAccountException;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Shiro登录相关控制器
// */
//@RestController
//public class ShiroController {
//
//    /**
//     * 登录处理
//     *
//     * @param userInfo
//     * @return
//     */
//    @RequestMapping("/login")
//    public JSONObject login(@RequestBody UserInfo userInfo) {
//        JSONObject jsonObject = new JSONObject();
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUsername(), userInfo.getPassword());
//
//        try {
//            subject.login(token);
//            jsonObject.put("token", subject.getSession().getId());
//            jsonObject.put("1001", "登录成功");
//        } catch (IncorrectCredentialsException e) {
//            jsonObject.put("1001", "密码错误");
//        } catch (LockedAccountException e) {
//            jsonObject.put("1003", "登录失败，该用户已被冻结");
//        } catch (AuthenticationException e) {
//            jsonObject.put("1004", "该用户不存在");
//        } catch (Exception e) {
//            jsonObject.put("1000", "登录异常，请稍后重试");
//        }
//
//        return jsonObject;
//    }
//
//    /**
//     * 首页处理
//     *
//     * @return
//     */
//    @RequestMapping("/index")
//    public String home() {
//        Subject subject = SecurityUtils.getSubject();
//        UserInfo principal = (UserInfo) subject.getPrincipal();
//        System.out.println(principal);
//
//        return "Home页面";
//    }
//
//    /**
//     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
//     *
//     * @return
//     */
//    @RequestMapping(value = "/unAuth")
//    public String unAuth() {
//        System.out.println("用户未登录");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code", "1000000");
//        jsonObject.put("msg", "未登录");
//        return jsonObject.toString();
//    }
//
//    /**
//     * 退出登录
//     *
//     * @return
//     */
//    @GetMapping("/logout")
//    public String logout() {
//        System.out.println("用户退出登录");
//        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.getPrincipal());
//        subject.logout();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code", "1000001");
//        jsonObject.put("msg", "退出登录");
//        return jsonObject.toString();
//    }
//
////    /**
////     * 清除用户当前权限缓存
////     */
////    @RequestMapping("/clearCachedAuthorizationInfo")
////    public void clearCachedAuthorizationInfo() {
////        RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
////        MyShiroRealm myShiroRealm = (MyShiroRealm) realmSecurityManager.getRealms().iterator().next();
////        myShiroRealm.clearCachedAuthorizationInfo();
////    }
//}
