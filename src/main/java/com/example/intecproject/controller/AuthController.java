package com.example.intecproject.controller;

import com.example.intecproject.config.CookieAuthenticationFilter;
import com.example.intecproject.model.DTO.LoginRequestDto;
import com.example.intecproject.model.DTO.RegisterUserDto;
import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.service.impl.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.intecproject.model.User;
import com.example.intecproject.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;



@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;

    private final AuthenticationService authenticationService;
    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@AuthenticationPrincipal UserDTO loginRequestDto, HttpServletResponse response)
    {
 Cookie cookie=new Cookie(CookieAuthenticationFilter.COOKIE_NAME,authenticationService.createToken(loginRequestDto));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Duration.of(1, ChronoUnit.DAYS).toSecondsPart());
        cookie.setPath("/");

        response.addCookie(cookie);


        return ResponseEntity.ok(loginRequestDto);
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




    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(HttpServletRequest request)
    {

        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

}
