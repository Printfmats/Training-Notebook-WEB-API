//package com.example.demo.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtDecoder jwtDecoder;
//
//    @Autowired
//    public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
//        this.jwtDecoder = jwtDecoder;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = extractTokenFromRequest(request);
//
//        if (token != null) {
//            try {
//                Authentication authentication = getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (JwtException e) {
//                // Obsłuż błąd uwierzytelniania
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String extractTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//
//    private Authentication getAuthentication(String token) {
//        Jwt jwt = jwtDecoder.decode(token);
//        String username = jwt.getSubject();
//        Collection<? extends GrantedAuthority> authorities = extractAuthoritiesFromJwt(jwt);
//        return new UsernamePasswordAuthenticationToken(username, token, authorities);
//    }
//
//    private Collection<? extends GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
//        List<String> role = jwt.getClaimAsStringList("role");
//        return role.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//}
