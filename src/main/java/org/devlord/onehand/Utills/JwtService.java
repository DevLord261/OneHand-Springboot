package org.devlord.onehand.Utills;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.devlord.onehand.User.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtService {

    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String GenerateToken(Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();
        return Jwts.builder().subject(user.getUsername())
                .claim("id",user.getId())
                .claim("email",user.getEmail())
                .claim("firstname",user.getFirstname())
                .claim("lastname",user.getLastname())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token,UserDetails user){
        try{
            String username = ExtractUsername(token);
            return username.equals(user.getUsername()) && !isExpired(token);
        }catch (JwtException e){
            return false;
        }
    }

    public String ExtractUsername(String token){
        try{
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getSubject();
        }catch (JwtException e) {
            return null;
        }
    }

    public boolean isExpired(String token){
        try{
            var expiration = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getExpiration();
            return expiration.before(new Date());
        }catch (JwtException e){
            return false;
        }
    }
}
