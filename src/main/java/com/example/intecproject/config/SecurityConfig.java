package com.example.intecproject.config;
/*
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.exceptionHandling().
                authenticationEntryPoint(userAuthenticationEntryPoint).
                and().addFilterBefore(new UsernamePasswordAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthenticationFIlter(),UsernamePasswordAuthenticationFilter.class)
                .csrf().disable().sessionManagement()
    }
}


 */