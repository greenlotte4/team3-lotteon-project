package com.lotteon.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("inId")
                .passwordParameter("password")
                .loginProcessingUrl("/user/login") // 로그인 처리 URL
                .defaultSuccessUrl("/", true)
                .failureHandler(new CustomAuthFailureHandler())
                .failureUrl("/user/login?error=true")
                .and()
                .formLogin()
                .loginPage("/seller/login")
                .usernameParameter("inId")
                .passwordParameter("password")
                .loginProcessingUrl("/seller/login") // 로그인 처리 URL
                .successHandler(new CustomAuthSuccessHandler()) // CustomAuthSuccessHandler 사용
                .failureUrl("/seller/login?error=true"); // 로그인 실패 시 이동할 페이지


        // 세션 설정
        http.sessionManagement(session -> session
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)  // 세션 고정 공격 방지
                .maximumSessions(1)  // 동시 로그인 세션 하나로 제한
                .maxSessionsPreventsLogin(true)  // 새로운 로그인이 기존 세션을 만료시키지 않도록 설정

        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/?success=101")
        );

        // 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/seller/login").permitAll()
                .requestMatchers("/admin/**").hasAnyRole("ADMIN","SELLER")
                .requestMatchers("/admin/faq/**").hasRole("ADMIN")
                .requestMatchers("/admin/qna/**").hasRole("ADMIN")
                .requestMatchers("/admin/notice/**").hasRole("ADMIN")
                .requestMatchers("/admin/store/**").hasRole("ADMIN")
                .requestMatchers("/admin/user/**").hasRole("ADMIN")
                .requestMatchers("/seller/**").hasAnyRole("ADMIN","SELLER")
                .requestMatchers("/article/**").permitAll()
                .requestMatchers("/company/**").permitAll()
                .requestMatchers("/policy/**").permitAll()
                .requestMatchers("/cs/**").permitAll()
                .requestMatchers("/cart").authenticated()
                .requestMatchers("/article/delete/**").authenticated()
                .anyRequest().permitAll()
        );

        // CSRF 비활성화 (필요에 따라 활성화 가능)
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }
}
