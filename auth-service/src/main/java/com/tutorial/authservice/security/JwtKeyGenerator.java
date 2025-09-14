package com.tutorial.authservice.security;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtKeyGenerator {

    private final SecretKey signingKey;

    public JwtKeyGenerator() {
        
        this.signingKey = Jwts.SIG.HS256.key().build();
    }

    public SecretKey getSigningKey() {
        return signingKey;
    }
}