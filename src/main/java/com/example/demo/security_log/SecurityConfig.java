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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

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
                    formlogin.successForwardUrl("/api/dodaj-notatki");
                })
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");

                })
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/","/css/**","/register").permitAll();
                    auth.requestMatchers("/api/**").authenticated();

                })
//                .rememberMe(remember -> {
//                    remember.key("mykey");
//                    remember.alwaysRemember(true);
//                      remember
//
//                })
//                .rememberMe()
//                .key("rem-me-key")
//                .rememberMeParameter("remember") // it is name of checkbox at login page
//                .rememberMeCookieName("remember") // it is name of the cookie
//                .tokenValiditySeconds(180) // remember for number of seconds
//                .and()
                .csrf().disable()
                .userDetailsService(jpaUserDetailsService)
                .build();

    }
}
