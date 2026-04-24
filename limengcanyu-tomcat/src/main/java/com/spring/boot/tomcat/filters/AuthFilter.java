package com.spring.boot.tomcat.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.tomcat.wrappers.PostBodyReaderRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Enumeration<String> attributeNames = servletRequest.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            System.out.println("attribute name: " + name + " value: " + servletRequest.getAttribute(name));
        }

        // http get
        servletRequest.getParameterMap().forEach((key, value) -> System.out.println("parameter key: " + key + " value: " + value));

        if (servletRequest instanceof HttpServletRequest) {
            PostBodyReaderRequestWrapper requestWrapper = new PostBodyReaderRequestWrapper((HttpServletRequest) servletRequest);

            // http post
            String body = requestWrapper.getBody();
            System.out.println("doFilter body: " + body);

            if (!ObjectUtils.isEmpty(body)) {
                Map map = objectMapper.readValue(body, Map.class);
                System.out.println("doFilter map: " + map);
            }

            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
