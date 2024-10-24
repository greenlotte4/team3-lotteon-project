package com.lotteon.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .successHandler(customAuthSuccessHandler()) // 로그인 성공 핸들러 설정
                .failureUrl("/user/login?error=true")  // 로그인 실패 시 URL 수정
                .usernameParameter("inId")  // 로그인 시 사용할 파라미터 이름
                .passwordParameter("Password")  // 로그인 시 사용할 비밀번호 파라미터 이름
        );



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
