package com.example.intecproject.config;

import com.example.intecproject.service.impl.UserLogServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class ActivityLoggingFilter extends OncePerRequestFilter
{

    @Autowired
    private UserLogServiceImpl userLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ipAddress=request.getRemoteAddr();
        String uri=request.getRequestURI();
        String method=request.getMethod();

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username=auth !=null && auth.isAuthenticated() ? auth.getName() : "anonymous";


        userLogService.log(username,method+" "+uri,ipAddress);

        filterChain.doFilter(request,response);

    }
}
