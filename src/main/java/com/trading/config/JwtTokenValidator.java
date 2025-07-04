package com.trading.config;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        String jwt= request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt!=null){
            jwt=jwt.substring(7); //'Bearer token'
            
            try{
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                Claims claims= Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
                
                String email=String.valueOf(claims.get("email"));

                String authorities=String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> authoritiesList= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication auth= new UsernamePasswordAuthenticationToken(
                    email,
                 null, //difference ici
                 authoritiesList);

                 SecurityContextHolder.getContext().setAuthentication(auth);
            }catch (Exception e){
                throw new RuntimeException("invalid token...", e);
            }
        
        }
        filterChain.doFilter(request, response);
    }
}
