package com.example.intecproject.config;

import com.example.intecproject.service.impl.AuthenticationService;
import com.example.intecproject.model.DTO.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter implements Ordered {

    private final AuthenticationService authenticationService;

    public JwtAuthorizationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                UserDTO user = authenticationService.findByToken(jwtToken);

                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, null // optionally pass authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (RuntimeException e) {
                // Invalid token: optionally log or ignore
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return SecurityProperties.BASIC_AUTH_ORDER - 2; // Before UsernamePasswordAuthFilter
    }
}