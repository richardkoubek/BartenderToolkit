package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @GetMapping("/register")
    public String registerForm(
            Model model,
            @RequestParam String email,
            @RequestParam String userName,
            @RequestParam String password
    ){

        return "registerUser";
    }
}
