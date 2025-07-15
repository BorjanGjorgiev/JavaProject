package com.example.intecproject.config;

import com.example.intecproject.model.DTO.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class UsernamePasswordAuthFilter extends OncePerRequestFilter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/api/auth/login".equals(path) && HttpMethod.POST.matches(request.getMethod())) {
            // Use ContentCachingRequestWrapper to cache the request body
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

            // Read the body without consuming it
            byte[] body = wrappedRequest.getContentAsByteArray();
            if (body.length > 0) {
                LoginRequestDto credentialsDto = MAPPER.readValue(body, LoginRequestDto.class);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                credentialsDto.getEmail(),
                                credentialsDto.getPassword()
                        )
                );
            }

            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}