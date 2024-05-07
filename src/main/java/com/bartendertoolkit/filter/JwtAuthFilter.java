package com.bartendertoolkit.filter;

import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
