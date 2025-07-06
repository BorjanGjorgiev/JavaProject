package com.example.intecproject.controller;

import com.example.intecproject.config.CookieAuthenticationFilter;
import com.example.intecproject.model.DTO.ChangePasswordDTO;
import com.example.intecproject.model.DTO.LoginRequestDto;
import com.example.intecproject.model.DTO.RegisterUserDto;
import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.service.impl.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.intecproject.model.User;
import com.example.intecproject.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class AuthController
{
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UserDTO userDTO = authenticationService.authenticate(loginRequestDto);


        String jwt = authenticationService.createAccessToken(userDTO);


        Cookie accessCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, jwt);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setMaxAge(24 * 60 * 60);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);


        String refreshToken = authenticationService.createRefreshToken(userDTO);


        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);


        userDTO.setToken(jwt);
        return ResponseEntity.ok(userDTO);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto dto)
    {
        User user = new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword())
        );
        userService.save(user);
        return ResponseEntity.ok("Registered successfully");
    }
    @PostMapping("/refresh")
    public ResponseEntity<UserDTO> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                                HttpServletResponse response)
    {
        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }
        UserDTO user;
        try {
            user = authenticationService.validateRefreshToken(refreshToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
        String newAccessToken = authenticationService.createAccessToken(user);
        String newRefreshToken = authenticationService.createRefreshToken(user);
        Cookie accessCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);
        response.addCookie(accessCookie);
        Cookie refreshCookie = new Cookie("refresh_token", newRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);
        user.setToken(newAccessToken);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(HttpServletResponse response)
    {
        Cookie accessCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, null);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);
        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(name = "auth_by_cookie", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found in cookie");
        }

        try {
            UserDTO user = authenticationService.findByToken(token);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto,@AuthenticationPrincipal User user)
    {
        try
        {
            userService.changePassword(user.getId(),dto.getOldPassword(),dto.getNewPassword());
            return ResponseEntity.ok("Password changed successfully!");
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}