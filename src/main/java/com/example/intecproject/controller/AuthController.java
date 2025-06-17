package com.example.intecproject.controller;

import com.example.intecproject.model.DTO.RegisterUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.intecproject.model.User;
import com.example.intecproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
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
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto dto)
    {

        User user=new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userService.save(user);
        return ResponseEntity.ok("Registered successfully");
    }

}
