package com.example.intecproject.service;

public interface JwtService {
    boolean isTokenValid(String token);
    String extractUsername(String token);
}
