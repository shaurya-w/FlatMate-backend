package com.example.mpr_backend_jan.config;

import com.example.mpr_backend_jan.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors() // use WebConfig CORS
                .and()
                .csrf().disable()  // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login", "/api/register", "/internal/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin().disable()  // disable default login page
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );

        System.out.println("SecurityFilterChain initialized");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            CustomUserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder
    ) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        System.out.println("AuthenticationManager initialized with CustomUserDetailsService and BCryptPasswordEncoder");

        return authBuilder.build();
    }
}