package com.trading.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    private JwtProvider() {
        // Empêche l'instanciation car tout est statique
    }

    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public static String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles=populateAuthorities(authorities);
        
        return jwtGenerator(key, roles, auth);
    }

    private static String jwtGenerator(final SecretKey key, final String roles, final Authentication authentication){
        return Jwts.builder()
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis()+86400000))
        .claim("email", authentication.getName())
        .claim("authorities", roles)
        .signWith(key)
        .compact();
    }

    public static String getEmailFromToken(String token){
        token= token.substring(7);
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        return getEmail(claims);
    }

    private static String getEmail(final Claims claims){
        return String.valueOf(claims.get("email"));
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority ga:authorities){
            auth.add(ga.getAuthority());
        }
        return String.join(",", auth);
    }
}
