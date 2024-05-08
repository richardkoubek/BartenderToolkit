package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.UserDetailsImpl;
import com.bartendertoolkit.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private final UserService userService;
    Long userId;
    private UserDetailsImpl loggedUserDetails;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUser(@RequestPart(value = "email", required = false) String email,
                                          @RequestPart(value = "password", required = false) String password)
            throws Exception {
        userService.validateNewUser(email, password);

        if (userService.existsByEmail(email)) {
            throw new Exception("User with this email already exists.");
        }
        
        return ResponseEntity.ok().build();
    }
}
