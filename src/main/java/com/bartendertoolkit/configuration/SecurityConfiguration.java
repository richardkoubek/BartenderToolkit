package com.bartendertoolkit.configuration;

import com.bartendertoolkit.filter.JwtAuthFilter;
import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

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

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        MvcRequestMatcher[] authWhiteList = {
                mvcMatcherBuilder.pattern("/")
        };

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authWhiteList)
                        .permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/**"))
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
