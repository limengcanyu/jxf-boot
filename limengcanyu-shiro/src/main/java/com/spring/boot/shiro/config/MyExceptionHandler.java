package com.spring.boot.shiro.config;//package com.spring.boot.shiro.config;
//
//import com.alibaba.fastjson.support.spring.FastJsonJsonView;
//import org.apache.shiro.authz.UnauthenticatedException;
//import org.apache.shiro.authz.UnauthorizedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Administrator on 2017/12/11.
// * 全局异常处理
// */
//@RestControllerAdvice
//public class MyExceptionHandler implements HandlerExceptionResolver {
//    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
//
//    @ExceptionHandler(Exception.class)
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
//        logger.debug("全局异常处理 resolveException");
//        ModelAndView mv = new ModelAndView();
//        FastJsonJsonView view = new FastJsonJsonView();
//        Map<String, Object> attributes = new HashMap<>();
//
//        if (ex instanceof UnauthenticatedException) {
//            logger.debug("未认证异常");
//            //未认证异常
//            attributes.put("code", "1000001");
//            attributes.put("msg", "token错误");
//        } else if (ex instanceof UnauthorizedException) {
//            logger.debug("未授权异常");
//            //未授权异常
//            attributes.put("code", "1000002");
//            attributes.put("msg", "用户无权限");
//        } else {
//            logger.debug("其他异常");
//            //其他异常
//            attributes.put("code", "1000003");
//            attributes.put("msg", ex.getMessage());
//        }
//
//        view.setAttributesMap(attributes);
//        mv.setView(view);
//        return mv;
//    }
//}
