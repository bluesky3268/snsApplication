package com.hyunbenny.snsApplication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String generateToken(String username, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(SignatureAlgorithm.HS256, getKey(key))
                .compact();
    }

    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String getUsername(String token, String key) {
        return extractClaimsFromToken(token, key).get("username", String.class);
    }

    public static boolean isExpiredToken(String token, String key) {
        Date expirationDate = extractClaimsFromToken(token, key).getExpiration();
        return expirationDate.before(new Date());
    }

    private static Claims extractClaimsFromToken(String token, String key) {
        return Jwts.parserBuilder()
                    .setSigningKey(getKey(key))
                    .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
