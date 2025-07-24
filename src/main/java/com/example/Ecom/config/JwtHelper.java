package com.example.Ecom.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    // Renamed for clarity (optional but recommended)
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Keep this as alias if you need backward compatibility
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Returns the email
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        final Date tokenExpirationDate = extractClaim(token, Claims::getExpiration);

        boolean emailMatch = Objects.equals(email, userDetails.getUsername()); // Compare with UserDetails username (which is email)
        boolean tokenIsExpired = tokenExpirationDate.before(new Date(System.currentTimeMillis()));

        return emailMatch && !tokenIsExpired;
    }

    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) // This is the email (set in UserDetailsServiceImpl)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14))
                .signWith(getSignInKey())
                .compact();
    }

    // Rest of the methods remain unchanged
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}