package com.example.intecproject.controller;

import org.springframework.ui.Model;
import com.example.intecproject.model.User;
import com.example.intecproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController
{

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String loginPage(Model model)
    {
        return "login";
    }


    @GetMapping("/register")
    public String processRegister(@ModelAttribute User user)
    {
        userService.save(user);
        return "redirect:/register?success";
    }





}
