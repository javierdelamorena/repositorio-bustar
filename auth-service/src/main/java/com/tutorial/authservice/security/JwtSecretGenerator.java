package com.tutorial.authservice.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        System.out.println("JWT Secret (Base64): " + Encoders.BASE64.encode(key.getEncoded()));
    }
}