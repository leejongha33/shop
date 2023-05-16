package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        // 시큐리티 처리에 HttpServletRequest 사용을 의미
        httpSecurity.authorizeRequests()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll() // permitAll -> 모든 사용자가 인증업이 해당 경로 접근 가능
                .mvcMatchers("/admin/**").hasRole("ADMIN") // admin 경로는 ADMIN 권한 유저만 접속가능
                .anyRequest().authenticated(); // 나머지 설정하지 않은 경로는 모두 사용자 인증 요구

        // 인증되지 않은 사용자가 리소스에 접근하였을때 수행되는 핸들러 등록
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Bean
    public void configure(WebSecurity web) throws Exception {
        // static 디렉토리 하위 파일은 인증 무시하도록 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }
}