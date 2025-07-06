package com.example.intecproject.config;
import com.example.intecproject.model.DTO.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component

public class UsernamePasswordAuthFilter
     extends OncePerRequestFilter implements Ordered
{
    private static final ObjectMapper MAPPER=new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/v1/signin".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod()))
        {
            LoginRequestDto credentialsDto = MAPPER.readValue(request.getInputStream(),LoginRequestDto.class);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(credentialsDto.getEmail(),credentialsDto.getPassword())
            );
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public int getOrder() {
        return SecurityProperties.BASIC_AUTH_ORDER - 1; // Just before BasicAuthenticationFilter
    }
}
