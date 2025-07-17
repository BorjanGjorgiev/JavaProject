package com.example.intecproject.config;

import com.example.intecproject.service.impl.AuthenticationService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final AuthenticationService authenticationService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(authenticationService);
    }
    @Bean
    public CookieAuthenticationFilter cookieAuthenticationFilter() {
        return new CookieAuthenticationFilter(authenticationService);
    }

    @Bean
    public ActivityLoggingFilter activityLoggingFilter() {
        return new ActivityLoggingFilter();
    }

    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint, AuthenticationService authenticationService) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(userAuthenticationEntryPoint))


                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(cookieAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(activityLoggingFilter(), CookieAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.deleteCookies(CookieAuthenticationFilter.COOKIE_NAME))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register", "/api/auth/change-password","/api/auth/signout").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/me").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/logs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/api").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/groups/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/groups/*/export").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/{id}/export").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/groups/{id}/import").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/groups/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/groups/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/groups/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/signout").permitAll()

                        .anyRequest().authenticated()
                )
                .build();
    }
}