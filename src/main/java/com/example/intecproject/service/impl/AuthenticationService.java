package com.example.intecproject.service.impl;
import com.example.intecproject.model.DTO.LoginRequestDto;
import com.example.intecproject.model.DTO.UserDTO;
import com.example.intecproject.model.User;
import com.example.intecproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class AuthenticationService
{

    private final PasswordEncoder passwordEncoder;

   private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    public AuthenticationService(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }
    public UserDTO authenticate(LoginRequestDto credentialsDto) {
       User user=userRepository.findByEmail(credentialsDto.getEmail()).orElseThrow(()->new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return UserDTO.fromUser(user);
    }
    public UserDTO findByLogin(String login) {
        User user=userRepository.findByEmail(login)
                .orElseThrow(()->new RuntimeException("Invalid email or password"));
        return UserDTO.fromUser(user);
    }
    public String createAccessToken(UserDTO user) {

        Date now=new Date();
        Date expiry=new Date(now.getTime()+15*60*1000);

        return Jwts.builder().setSubject(user.getLogin())
                .claim("id",user.getId())
                .setIssuedAt(now).
                setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512,secretKey.getBytes()).compact();
    }
    public UserDTO findByToken(String token) {
        try
        {
            Claims claims=Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token).getBody();

            String login=claims.getSubject();
            return findByLogin(login);
        }
        catch (JwtException e)
        {
            throw new RuntimeException("Invalid JWT token");
        }
    }


    public String createRefreshToken(UserDTO user)
    {
        Date now=new Date();

        Date expiry=new Date(now.getTime()+7L*24*60*60*1000);


        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512,secretKey.getBytes())
                .compact();

    }

    public UserDTO validateRefreshToken(String token)
    {
        try
        {
            Claims claims=Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String login=claims.getSubject();
            return findByLogin(login);
        }catch (JwtException e)
        {
            throw new RuntimeException("Invalid refresh token");
        }
    }

}
