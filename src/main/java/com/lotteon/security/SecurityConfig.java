package com.lotteon.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    // 어플리케이션 실행시 등록됨
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/login?error=true")  // 로그인 실패 시 URL 수정
                .usernameParameter("" +
                        "inId")  // 로그인 시 사용할 파라미터 이름
                .passwordParameter("Password"));  // 로그인 시 사용할 비밀번호 파라미터 이름

        http.sessionManagement(session -> session
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)  // 세션 고정 공격 방지
                .maximumSessions(1)  // 동시 로그인 세션 하나로 제한 (옵션)
                .maxSessionsPreventsLogin(true)  // 새로운 로그인이 기존 세션을 만료시키지 않도록 설정
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/?success=101"));

        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/admin/**").hasRole("SELLER")  // 관리자만 접근 가능
                .requestMatchers("/seller/**").hasRole("SELLER")  // 판매자만 접근 가능
                .requestMatchers("/article/**").permitAll()
                .requestMatchers("/company/**").permitAll()
                .requestMatchers("/policy/**").permitAll()
                .requestMatchers("/cs/**").permitAll()
                .requestMatchers("/cart").authenticated()
                .requestMatchers("/article/delete/**").authenticated()
                .anyRequest().permitAll());

        // CSRF 비활성화 - 필요시 활성화
        http.csrf(AbstractHttpConfigurer::disable);

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
}
