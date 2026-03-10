package com.example.xemphim.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service

public class Jwtservice {

    private final Key key;
    private final long expMinutes;

    public Jwtservice(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expMinutes}") long expMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMinutes = expMinutes;
    }

    public String generateToken(String subjectEmail, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subjectEmail)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return parseAllClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            Claims c = parseAllClaims(token);
            return c.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}