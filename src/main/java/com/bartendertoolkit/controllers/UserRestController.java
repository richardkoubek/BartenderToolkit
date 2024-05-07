package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserInfoService userInfoService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
}
