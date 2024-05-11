package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.UserDetailsImpl;
import com.bartendertoolkit.services.UserService;
import org.springframework.http.HttpStatus;
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
                                          @RequestPart(value = "password", required = false) String password,
                                          @RequestPart(value = "username", required = false) String userName)
            throws Exception {
        userService.validateNewUser(email, password);
        userService.checkIfExistsByEmailAndUserName(email, userName);

        userService.createNewUser(email, password, userName);
        this.userId = userService.findByEmail(email).getId();
        this.loggedUserDetails = new UserDetailsImpl(userId,email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
