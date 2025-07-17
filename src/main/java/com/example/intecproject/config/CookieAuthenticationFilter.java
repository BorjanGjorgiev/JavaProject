package com.example.intecproject.config;

import com.example.intecproject.model.Role;
import com.example.intecproject.model.User;
import com.example.intecproject.service.impl.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    public static final String COOKIE_NAME = "auth_by_cookie";

    private final AuthenticationService authenticationService;

    public CookieAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Optional<Cookie> cookieAuth = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        cookieAuth.ifPresent(cookie -> {
            try {
                String token = cookie.getValue();
                User user = authenticationService.findUserByToken(token); // validate and retrieve user

                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // ✅ FIX: Provide authorities from user's role
                    List<SimpleGrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

                    PreAuthenticatedAuthenticationToken auth =
                            new PreAuthenticatedAuthenticationToken(user, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // Optionally log error: invalid token, etc.
            }
        });

        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return SecurityProperties.BASIC_AUTH_ORDER - 3; // earlier than Spring BasicAuth
    }
}