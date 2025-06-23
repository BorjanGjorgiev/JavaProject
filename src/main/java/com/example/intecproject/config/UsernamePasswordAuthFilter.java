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
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
public class UsernamePasswordAuthFilter
     extends OncePerRequestFilter
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
}
