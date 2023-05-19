package com.example.demo.security_log;

import com.example.demo.jwt.RsaKeyProperties;
import com.example.demo.repositories.UserAccountRepo;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {
    private final JpaUserDetailsService jpaUserDetailsService;
    private final UserAccountRepo userAccountRepo;
    private final RsaKeyProperties rsaKeys;



    @Autowired
    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService, UserAccountRepo userAccountRepo, RsaKeyProperties rsaKeys) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.userAccountRepo = userAccountRepo;
        this.rsaKeys = rsaKeys;
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
                    formlogin.failureForwardUrl("/login");
                })
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");

                })
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers(request -> !request.getRequestURI().startsWith("/css/**")).permitAll();
                    auth.requestMatchers("/","/register","/password").permitAll();
                    auth.requestMatchers("/api/**").authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/login");
                    oauth2.successHandler(new CustomSuccessHandler(userAccountRepo));

                })
                .csrf().disable()
                .userDetailsService(jpaUserDetailsService)
//                .addFilterBefore(new JwtAuthenticationFilter(jwtDecoder), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }



}
