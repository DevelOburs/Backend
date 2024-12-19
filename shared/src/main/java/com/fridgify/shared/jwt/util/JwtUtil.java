package com.fridgify.shared.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "develobursberatiyedi"; // Change to a secure key
    private final long EXPIRATION_TIME = 10000 * 60 * 60; // 10 hours

    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, roles);
    }

    private String createToken(Map<String, Object> claims, String subject, List<String> roles) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
}
