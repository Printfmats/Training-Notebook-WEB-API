package com.example.demo.security_log;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/","/css/**","/register/**").permitAll().anyRequest().authenticated()
                .and()
//                .rememberMe()
//                .userDetailsService(jpaUserDetailsService)
//                .key("rem-me-key")
//                .rememberMeParameter("remember") // it is name of checkbox at login page
//                .rememberMeCookieName("rememberlogin") // it is name of the cookie
//                .tokenValiditySeconds(100) // remember for number of seconds
//                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successForwardUrl("/api/dodaj-notatki")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .userDetailsService(jpaUserDetailsService)
                .build();
    }
}
