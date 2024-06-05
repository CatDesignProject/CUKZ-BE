package com.example.security;

import com.example.security.authentication.CustomAuthenticationFailureHandler;
import com.example.security.authentication.CustomAuthenticationSuccessHandler;
import com.example.security.authentication.CustomLogoutSuccessHandler;
import com.example.security.authentication.LoginProcessingFilter;
import com.example.security.exception.CustomAccessDeniedHandler;
import com.example.security.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login.disable()) // 폼로그인 비허용
                .logout(logout -> logout
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/members/logout"))
                .requestCache(cache -> cache.disable()) // 요청 간 HttpSession 내 RequestCache 저장 비허용 (로그인 전 세션 생성 방지)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/**/admin/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/**/non-members/**")).permitAll()   // 비회원 요청 허용
                        .requestMatchers("/products/{productId:\\d+}/likes", "/products/{productId:\\d+}/unlikes",
                                "/members/likes", "/reviews/members/{sellerId:\\d+}/purchaseForm/{purchaseFormId:\\d+}").hasAuthority("ROLE_USER")
                        .requestMatchers("/members/login", "/members/register", "/members/verify-username", "/verify-email",
                                "/products/search", "/products/paging", "/products/{productId:\\d+}", "/reviews/members/{sellerId:\\d+}").permitAll()
                        .anyRequest().authenticated())
                .securityContext(securityContext -> new HttpSessionSecurityContextRepository())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain addCustomFilters(HttpSecurity http,
                                                LoginProcessingFilter loginProcessingFilter) throws Exception {
        http
                .addFilterAt(loginProcessingFilter, UsernamePasswordAuthenticationFilter.class); // 로그인 필터 등록

        return http.build();
    }

    @Bean
    public LoginProcessingFilter loginProcessingFilter(AuthenticationManager authenticationManager) {
        LoginProcessingFilter loginProcessingFilter = new LoginProcessingFilter();
        loginProcessingFilter.setAuthenticationManager(authenticationManager);
        loginProcessingFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        loginProcessingFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        return loginProcessingFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
