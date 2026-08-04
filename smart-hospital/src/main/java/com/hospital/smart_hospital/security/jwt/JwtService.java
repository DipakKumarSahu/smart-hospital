package com.hospital.smart_hospital.security.jwt;


import com.hospital.smart_hospital.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    /**
     * Generates JWT without additional claims.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    /**
     * Generates JWT with additional claims.
     */
    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {

        Date issuedAt = new Date();
        Date expiration = new Date(
                issuedAt.getTime() + jwtProperties.getExpiration()
        );

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts username(email) from JWT.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validates JWT against the authenticated user.
     */
    public boolean isTokenValid(String token,
                                UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Returns all claims present in JWT.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Checks whether the token is expired.
     */
    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Creates signing key from Base64 encoded secret.
     */
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }
}