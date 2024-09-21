package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.models.LocalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@UtilityClass
public class TokenUtils {
    /**
     * Generates a token.
     * @param localUser the local user
     * @param secret the secret
     * @param expirationSeconds the expiration seconds
     * @return the generated token
     */
    public static String generateToken(LocalUser localUser,
                                        String secret,
                                        int expirationSeconds) {
        Date now = new Date();
        long expirationMillis = expirationSeconds * 1000L;
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(localUser.getUserEntity().getEmail())
                .setExpiration(expiryDate)
                .setIssuedAt(now)
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS512).compact();
    }

    /**
     * Gets the signing key.
     * @param secret the secret
     * @return the signing key
     */
    private static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        byte[] base64KeyBytes = Base64.getEncoder().encode(keyBytes);
        return Keys.hmacShaKeyFor(base64KeyBytes);
    }

    /**
     * Extracts the token from the header.
     * @param header the header value
     * @return the extracted token
     */
    public static String extractTokenFromHeader(String header) {
        return header.substring(7);
    }
}
