package com.macbul.platform.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    private final SecretKey key;
    private final long accessMs;
    private final long refreshMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expiration-ms}") long accessMs,
            @Value("${jwt.refresh.expiration-ms}") long refreshMs
    ){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessMs = accessMs;
        this.refreshMs = refreshMs;
    }

    public String generateAccessToken(UserDetails user){
        return buildToken(Map.of("type","access"), user.getUsername(), accessMs);
    }
    public String generateRefreshToken(UserDetails user){
        return buildToken(Map.of("type","refresh"), user.getUsername(), refreshMs);
    }

    private String buildToken(Map<String,Object> claims, String subject, long ttl){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttl))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isAccessToken(String token){
        Object t = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("type");
        return "access".equals(t);
    }

    public boolean isTokenValid(String token, UserDetails user){
        try{
            final String username = extractUsername(token);
            Date exp = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getExpiration();
            return username.equals(user.getUsername()) && exp.after(new Date());
        }catch (JwtException e){ return false; }
    }
}
