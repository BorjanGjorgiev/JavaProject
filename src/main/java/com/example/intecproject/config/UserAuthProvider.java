package com.example.intecproject.config;

import com.example.intecproject.model.DTO.LoginRequestDto;
import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.model.User;
import com.example.intecproject.repository.UserRepository;
import com.example.intecproject.service.impl.AuthenticationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Collections;

@Component
public class UserAuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserAuthProvider(UserRepository userRepository,
                            @Lazy AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            // Handle form login
            UserDTO userDTO = authenticationService.authenticate(new LoginRequestDto(
                    (String) authentication.getPrincipal(),
                    (String) authentication.getCredentials()
            ));

            User user = null;
            try {
                user = userRepository.findByEmail(userDTO.getLogin())
                        .orElseThrow(() -> new AuthenticationException("User not found") {});
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }

            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.emptyList()
            );
        }
        else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            // Handle JWT token
            String token = (String) authentication.getPrincipal();
            UserDTO userDTO = authenticationService.findByToken(token);

            if (userDTO != null) {
                User user = null;
                try {
                    user = userRepository.findByEmail(userDTO.getLogin())
                            .orElseThrow(() -> new AuthenticationException("User not found") {});
                } catch (AuthenticationException e) {
                    throw new RuntimeException(e);
                }

                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList()
                );
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication) ||
                PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}