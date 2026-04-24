package com.spring.boot.shiro.config;//package com.spring.boot.shiro.config;
//
//import com.spring.boot.shiro.dao.entity.UserInfo;
//import com.spring.boot.shiro.service.SysPermissionService;
//import com.spring.boot.shiro.service.SysRoleService;
//import com.spring.boot.shiro.service.UserInfoService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.annotation.Resource;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by Administrator on 2017/12/11.
// * 自定义权限匹配和账号密码匹配
// */
//public class MyShiroRealm extends AuthorizingRealm {
//    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);
//    @Resource
//    private SysRoleService sysRoleService;
//
//    @Resource
//    private SysPermissionService sysPermissionService;
//
//    @Resource
//    private UserInfoService userInfoService;
//
//    /**
//     * 清楚用户权限缓存
//     * 设置角色生效，清除旧权限缓存，使新权限缓存生效
//     *
//     */
//    public void clearCachedAuthorizationInfo() {
//        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
//    }
//
//    /**
//     * shiro的权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo();
//     * 当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行，所以如果只是简单的身份认证没有权限的控制的话，
//     * 那么这个方法可以不进行实现，直接返回null即可。在这个方法中主要是使用类：SimpleAuthorizationInfo进行角色的添加和权限的添加
//     *
//     * @param principals
//     * @return
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        logger.debug("权限 --> MyShiroRealm.doGetAuthorizationInfo()");
//        logger.debug("PrimaryPrincipal: {}", principals.getPrimaryPrincipal());
//
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        Set<String> roles = new HashSet<>();
//        roles.add("admin");
//
//        authorizationInfo.setRoles(roles);
//
//        Set<String> stringPermissions = new HashSet<>();
//        stringPermissions.add("insert");
//        stringPermissions.add("update");
//
//        authorizationInfo.setStringPermissions(stringPermissions);
//
////        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
////        try {
////            List<SysRole> roles = sysRoleService.selectRoleByUser(userInfo);
////            for (SysRole role : roles) {
////                authorizationInfo.addRole(role.getRole());
////            }
////            List<SysPermission> sysPermissions = sysPermissionService.selectPermByUser(userInfo);
////            for (SysPermission perm : sysPermissions) {
////                authorizationInfo.addStringPermission(perm.getPermission());
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        //返回用户权限信息
//        return authorizationInfo;
//    }
//
//    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        logger.debug("认证 --> MyShiroRealm.doGetAuthenticationInfo()");
//
//        //将传入token转换为UsernamePasswordToken
//        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
//
//        //打印登录用户信息
//        String username = usernamePasswordToken.getUsername();
//        logger.debug("username: {}", username);
//        logger.debug("password: {}", usernamePasswordToken.getPassword());
//        logger.debug("principal: {}", usernamePasswordToken.getPrincipal());
//        logger.debug("credentials: {}", usernamePasswordToken.getCredentials());
//
//        //根据登录用户username从数据库中查询用户信息
//        UserInfo userInfo = userInfoService.findByUserName(username);
//        logger.debug("userInfo username: {} password: {} credentialsSalt: {}", userInfo.getUsername(), userInfo.getPassword(), userInfo.getCredentialsSalt());
//
//        //生成认证对象
//        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                userInfo, //数据库中用户
//                userInfo.getPassword(), //用户密码
//                ByteSource.Util.bytes(userInfo.getCredentialsSalt()), //用户密码盐
//                getName() //当前Realm名称
//        );
//
//        //返回用户认证对象
//        return authenticationInfo;
//    }
//
//}
