package org.asura.ai.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.asura.ai.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /** 测试Token白名单 - 用于测试环境直接访问 */
    private static final List<String> TEST_TOKENS = Arrays.asList(
            "asura-ai-test-token",
            "test-token-123",
            "qa-test-token"
    );

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        
        // 跳过不需要认证的路径
        if (requestUri.startsWith("/api/auth/register") || requestUri.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                // 检查是否是测试Token
                if (isTestToken(jwt)) {
                    logger.info("使用测试Token访问，用户: testuser");
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            "testuser", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }

                // 检查token是否已注销
                if (AuthServiceImpl.isTokenInvalidated(jwt)) {
                    logger.warn("Token已失效，用户已退出登录");
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "用户已退出登录，请重新登录");
                    return;
                } else if (tokenProvider.validateToken(jwt)) {
                    String username = tokenProvider.getUsernameFromToken(jwt);
                    String role = tokenProvider.getRoleFromToken(jwt);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token无效，请重新登录");
                    return;
                }
            } else {
                // 没有提供token
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
                return;
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "认证失败，请重新登录");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查是否是测试Token
     */
    private boolean isTestToken(String token) {
        return TEST_TOKENS.contains(token);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\",\"status\":" + statusCode + "}");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}