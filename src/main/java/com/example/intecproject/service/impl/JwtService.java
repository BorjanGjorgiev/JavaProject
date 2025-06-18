package com.example.intecproject.service.impl;

public class JwtService implements com.example.intecproject.service.JwtService
{
    @Override
    public boolean isTokenValid(String token) {
        return false;
    }

    @Override
    public String extractUsername(String token) {
        return null;
    }
}
