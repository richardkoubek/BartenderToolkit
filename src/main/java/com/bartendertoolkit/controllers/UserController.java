package com.bartendertoolkit.controllers;

import com.bartendertoolkit.models.User;
import com.bartendertoolkit.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public String home(
            @PathVariable Long userId
    ){
        var userEnt =  userService.findById(userId);
        if(userEnt.isEmpty()){
            return String.valueOf(ResponseEntity.notFound().build());
        }


        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());

        return "registerUser";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String email,
            @RequestParam String userName,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ){
        var userEnt = userService.createNewUser(email, userName, password);
        if(userEnt.isEmpty()){
            redirectAttributes.addFlashAttribute("userSuccess", false);
        } else {
            redirectAttributes.addFlashAttribute("userSuccess", true);
            redirectAttributes.addFlashAttribute("userName", userEnt.get().getUserName());
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("login", new User());

        return "loginUser";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String userName,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ){
        var userEnt = userService.findUserByUsernameAndPassword(userName, password);
        if(userEnt.isEmpty()){
            redirectAttributes.addFlashAttribute("login", false);
        } else {
            redirectAttributes.addFlashAttribute("login", true);
            redirectAttributes.addFlashAttribute("userName", userEnt.get().getUserName());
        }

        return "redirect:/" + userEnt.get().getId();
    }

    @PostMapping("/logout/{userId}")
    public String logoutUser(
            RedirectAttributes redirectAttributes,
            @PathVariable Long userId
    ){
        redirectAttributes.addFlashAttribute("logout", true);
        redirectAttributes.addFlashAttribute("userName", userService.findById(userId));

        return "redirect:/";
    }
}
