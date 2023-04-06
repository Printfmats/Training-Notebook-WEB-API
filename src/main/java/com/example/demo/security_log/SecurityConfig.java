package com.example.demo.security_log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {
    private final JpaUserDetailsService jpaUserDetailsService;

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
                .formLogin(formlogin ->{
                    formlogin.loginPage("/login").permitAll();
                    formlogin.successForwardUrl("/api/treningi");
                })
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");

                })
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers(request -> !request.getRequestURI().startsWith("/css/**")).permitAll();
                    auth.requestMatchers("/","/register").permitAll();
                    auth.requestMatchers("/api/**").authenticated();

                })
                .csrf().disable()
                .userDetailsService(jpaUserDetailsService)
                .build();

    }
}
