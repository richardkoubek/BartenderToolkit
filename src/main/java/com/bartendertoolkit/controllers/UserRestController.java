package com.bartendertoolkit.controllers;

import com.bartendertoolkit.models.User;
import com.bartendertoolkit.services.AuthService;
import com.bartendertoolkit.services.UserDetailsImpl;
import com.bartendertoolkit.services.UserService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final AuthService authService;
    Long userId;
    private UserDetailsImpl loggedUserDetails;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUser(@RequestPart(value = "email", required = false) String email,
                                          @RequestPart(value = "password", required = false) String password,
                                          @RequestPart(value = "username", required = false) String userName)
            throws Exception {
        userService.validateNewUser(email, password);
        userService.checkIfExistsByEmailAndUserName(email, userName);

        User user = userService.createNewUser(email, password, userName);
        String token = authService.generateAuthToken(user);
        userService.setTokenToUser(user, token);

        this.userId = userService.findByEmail(email).getId();
        this.loggedUserDetails = new UserDetailsImpl(userId, email, password);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/login", consumes = {"multipart/form-data"})
    public ResponseEntity<?> logInUser(@RequestPart(value = "email", required = false) String email,
                                       @RequestPart(value = "password", required = false) String password
    ) {
        try {
            userService.checkCredentials(email);
            Cookie authCookie = authService.login(email, password);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Set-Cookie", authCookie.toString());
            this.userId = userService.findByEmail(email).getId();
            this.loggedUserDetails = new UserDetailsImpl(userId, email, password);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .headers(responseHeaders)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOutUser(@CookieValue(name = "authCookie", required = false) String authCookieValue) throws UnsupportedEncodingException {
        return authService.logoutUserFromCookie(authCookieValue);
    }
}
