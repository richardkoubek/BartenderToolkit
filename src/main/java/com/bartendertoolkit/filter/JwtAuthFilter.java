package com.bartendertoolkit.filter;

import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtAuthFilter extends OncePerRequestFilter {
    @Value("${authCookieName}")
    private String jwtCookie;
    private AuthService authService;
    private UserService userService;
    public JwtAuthFilter(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Claims claims = null;

        if (cookies != null){
            Optional<Cookie> cookie = Arrays.stream(cookies)
                    .filter(name -> name.getName().equals(jwtCookie))
                    .findFirst();

            if (cookie.isPresent()){
                String token = cookie.get().getValue();
                if(authService.validateToken(token)) {
                    claims = authService.getAllClaimsFromToken(token);
                }
            }
        }

    }
}
