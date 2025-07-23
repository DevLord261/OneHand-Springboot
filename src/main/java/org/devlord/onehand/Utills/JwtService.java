package org.devlord.onehand.Utills;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.devlord.onehand.User.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtService {

    private static final long EXPIRATION_MS = 1000 * 60 * 60 *24;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String GenerateToken(Authentication auth){
        UserEntity user = (UserEntity) auth.getPrincipal();
        return Jwts.builder().subject(user.getUsername())
                .claim("id",user.getId())
                .claim("email",user.getEmail())
                .claim("firstname",user.getFirstname())
                .claim("lastname",user.getLastname())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_MS))
                .signWith(getSigningKey())
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
                    .verifyWith(getSigningKey())
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
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getExpiration();
            return expiration.before(new Date());
        }catch (JwtException e){
            return false;
        }
    }
}
