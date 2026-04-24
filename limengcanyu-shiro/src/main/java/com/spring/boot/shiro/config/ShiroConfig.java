package com.spring.boot.shiro.config;//package com.spring.boot.shiro.config;
//
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.codec.Base64;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.crazycake.shiro.RedisCacheManager;
//import org.crazycake.shiro.RedisManager;
//import org.crazycake.shiro.RedisSessionDAO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * Created by Administrator on 2017/12/11.
// */
//@Configuration
//public class ShiroConfig {
//    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);
//
//    @Bean
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        logger.debug("ShiroConfiguration.shirFilter()");
//
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//        // 前后端分离时登录界面跳转应由前端路由控制，后台仅返回json数据
//        // 登录URL，如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 登录成功后要跳转的URL
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        // 未授权URL
//        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");
//
//        // 设置SecurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        //filterChainDefinitionMap必须是LinkedHashMap因为它必须保证有序
//        // anon:  不需要认证就能访问
//        // authc: 需要认证才能访问
//        // user:  配置记住我或认证通过可以访问
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//
//        //注意过滤器配置顺序 不能颠倒
//        //登录，不需要认证就可以访问
//        filterChainDefinitionMap.put("/login", "anon");
//        //退出登录，其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "anon");
//
//        //静态资源所有url都都可以匿名访问
//        filterChainDefinitionMap.put("/static/**", "anon");
//
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/druid/**", "anon"); // 访问druid页面，表示可以匿名访问
//
//        //一般将 /**放在最为下边 -->: 这是一个坑呢，一不小心代码就不好使了
//        filterChainDefinitionMap.put("/**", "authc"); //表示需要认证才可以访问
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//
//        logger.debug("登录URL: {} 登录成功后跳转到URL: {} 未授权URL: {}",
//                shiroFilterFactoryBean.getLoginUrl(),
//                shiroFilterFactoryBean.getSuccessUrl(),
//                shiroFilterFactoryBean.getUnauthorizedUrl()
//        );
//
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        // 自定义session管理 使用redis
//        securityManager.setSessionManager(sessionManager());
//        // 自定义缓存实现 使用redis
//        securityManager.setCacheManager(cacheManager());
//
//        //注入记住我管理器;
//        securityManager.setRememberMeManager(rememberMeManager());
//
//        return securityManager;
//    }
//
//    @Bean
//    public MyShiroRealm myShiroRealm() {
//        MyShiroRealm myShiroRealm = new MyShiroRealm();
//        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return myShiroRealm;
//    }
//
//    /**
//     * 凭证匹配器
//     * 密码校验由Shiro的SimpleAuthenticationInfo进行处理
//     * 1.MyShiroRealm中的认证方法doGetAuthenticationInfo返回从数据库中查询的认证对象
//     * 2.调用HashedCredentialsMatcher的doCredentialsMatch方法对登录用户的密码和数据库中返回的认证对象的登录密码进行校验
//     *
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(1024);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }
//
//    //自定义sessionManager
//    @Bean
//    public SessionManager sessionManager() {
//        MySessionManager mySessionManager = new MySessionManager();
//        mySessionManager.setSessionDAO(redisSessionDAO());
//        return mySessionManager;
//    }
//
//    /**
//     * 配置shiro redisManager
//     * <p>
//     * 使用的是shiro-redis开源插件
//     *
//     * @return
//     */
//    @ConfigurationProperties(prefix = "redis.shiro")
//    public RedisManager redisManager() {
//        return new RedisManager();
//    }
//
//    /**
//     * cacheManager 缓存 redis实现
//     * <p>
//     * 使用的是shiro-redis开源插件
//     *
//     * @return
//     */
//    @Bean
//    public RedisCacheManager cacheManager() {
//        RedisCacheManager redisCacheManager = new RedisCacheManager();
//        redisCacheManager.setRedisManager(redisManager());
//        return redisCacheManager;
//    }
//
//    /**
//     * RedisSessionDAO shiro sessionDao层的实现 通过redis
//     * <p>
//     * 使用的是shiro-redis开源插件
//     */
//    @Bean
//    public RedisSessionDAO redisSessionDAO() {
//        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
//        redisSessionDAO.setRedisManager(redisManager());
////      Custom your redis key prefix for session management, if you doesn't define this parameter,
////      shiro-redis will use 'shiro_redis_session:' as default prefix
////      redisSessionDAO.setKeyPrefix("");
//        return redisSessionDAO;
//    }
//
//    /**
//     * 记住密码Cookie
//     */
//    @Bean
//    public SimpleCookie rememberMeCookie() {
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//        simpleCookie.setHttpOnly(true);
//        simpleCookie.setMaxAge(7 * 24 * 60 * 60);//7天
//        return simpleCookie;
//    }
//
//    /**
//     * cookie管理对象;记住我功能
//     * @return
//     */
//    public CookieRememberMeManager rememberMeManager(){
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
//        cookieRememberMeManager.setCookie(rememberMeCookie());
//        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
//        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
//        return cookieRememberMeManager;
//    }
//
//    /**
//     * Shiro生命周期处理器:
//     * 用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调(例如:UserRealm)
//     * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调(例如:DefaultSecurityManager)
//     */
//    @Bean
//    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
//     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
//     *
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    /**
//     * springboot shiro开启注解
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }
//
//    /**
//     * 注册全局异常处理
//     * @return
//     */
//    @Bean(name = "exceptionHandler")
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return new MyExceptionHandler();
//    }
//
//}
