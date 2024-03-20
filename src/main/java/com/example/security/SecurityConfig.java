package com.example.security;

import com.example.security.authentication.LoginProcessingFilter;
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
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .securityContext(securityContext -> new HttpSessionSecurityContextRepository());

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
