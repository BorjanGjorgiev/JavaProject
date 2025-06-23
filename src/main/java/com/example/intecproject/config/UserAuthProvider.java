package com.example.intecproject.config;

import com.example.intecproject.model.DTO.LoginRequestDto;
import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.service.impl.AuthenticationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAuthProvider implements AuthenticationProvider
{

private final AuthenticationService authenticationService;

    public UserAuthProvider(@Lazy AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDTO userDTO=null;

        if(authentication instanceof UsernamePasswordAuthenticationToken)
        {
            userDTO =authenticationService.authenticate(new LoginRequestDto(
                    (String) authentication.getPrincipal(),
                    (String) authentication.getCredentials()
            ));
        }
        else if(authentication instanceof PreAuthenticatedAuthenticationToken)
        {
            userDTO=authenticationService.findByToken((String) authentication.getPrincipal());
        }
        if(userDTO==null)
        {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDTO,null, Collections.emptyList());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
