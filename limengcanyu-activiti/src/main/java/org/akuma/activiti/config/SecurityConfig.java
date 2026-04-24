package org.akuma.activiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "ACTIVITI_USER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER", "ACTIVITI_USER")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager123"))
                .roles("MANAGER", "ACTIVITI_USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user, manager);
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authz -> authz
//                .requestMatchers("/api/workflow/**").hasRole("ACTIVITI_USER")
//                .requestMatchers("/actuator/**").hasRole("ADMIN")
//                .requestMatchers("/h2-console/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .httpBasic()
//            .and()
//            .csrf().disable()
//            .headers().frameOptions().disable(); // 允许H2控制台
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/workflow/**").hasRole("ACTIVITI_USER")
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {}) // 替代旧的.httpBasic().and()风格
                .csrf(AbstractHttpConfigurer::disable) // Lambda表达式配置CSRF
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // Lambda表达式配置FrameOptions
                );

        return http.build();
    }

}