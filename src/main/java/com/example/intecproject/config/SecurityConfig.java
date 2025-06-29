package com.example.intecproject.config;

import com.example.intecproject.config.CookieAuthenticationFilter;
import com.example.intecproject.config.UserAuthenticationEntryPoint;
import com.example.intecproject.config.UsernamePasswordAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthenticationFilter(), UsernamePasswordAuthFilter.class)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.deleteCookies(CookieAuthenticationFilter.COOKIE_NAME))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/change-password").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/api").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/me").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/signout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/groups/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/groups/*/export").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/{id}/export").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/groups/{id}/import").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/groups/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/groups/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/groups/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/signout").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/logs").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}