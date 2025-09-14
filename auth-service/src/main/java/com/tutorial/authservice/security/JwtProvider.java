package com.tutorial.authservice.security;

import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.tutorial.authservice.dto.RequestDto;
import com.tutorial.authservice.entity.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtProvider {

    private final RouteValidator routeValidator;
    private final SecretKey signingKey;

    public JwtProvider(RouteValidator routeValidator, JwtKeyGenerator keyGenerator) {
        this.routeValidator = routeValidator;
        this.signingKey = keyGenerator.getSigningKey();
    }

    public String createToken(AuthUser authUser) {
        var claims = new HashMap<String, Object>();
        claims.put("id", authUser.getId());
        claims.put("role", authUser.getRole());

        var now = new Date();
        var exp = new Date(now.getTime() + 3600_000); // 1 hora

        return Jwts.builder()
                .subject(authUser.getUserName())
                .claims(claims)
                .issuedAt(now)
                .expiration(exp)
                .signWith(signingKey)
                .compact();
    }

    public boolean validate(String token, RequestDto dto) {
        try {
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
        } catch (Exception e) {
            return false;
        }
        return !(!isAdmin(token) && routeValidator.isAdminPath(dto));
    }

    public String getUserNameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return "bad token";
        }
    }

    private boolean isAdmin(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return "admin".equals(claims.get("role"));
        } catch (Exception e) {
            return false;
        }
    }
}
