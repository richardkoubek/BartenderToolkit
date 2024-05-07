package com.bartendertoolkit.configuration;

import com.bartendertoolkit.filter.JwtAuthFilter;
import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthService authService;
    private final UserService userService;

    @Bean
    public JwtAuthFilter authenticationTokenFilterBean() {
        return new JwtAuthFilter(authService, userService);
    }


}
