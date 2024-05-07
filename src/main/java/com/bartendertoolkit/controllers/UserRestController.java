package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserDetailsImpl userDetails;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
}
