package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.models.LocalUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@UtilityClass
public class TokenUtils {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Encoder base64Encoder = Base64.getEncoder();

    /**
     * Generates a token.
     *
     * @param localUser         the local user
     * @param secret            the secret
     * @param expirationSeconds the expiration seconds
     * @param clock
     * @return the generated token
     */
    public static String generateToken(LocalUser localUser,
                                       String secret,
                                       int expirationSeconds,
                                       Date now) {
        long expirationMillis = expirationSeconds * 1000L;
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(localUser.getUserEntity().getEmail())
                .setExpiration(expiryDate)
                .setIssuedAt(now)
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS512).compact();
    }

    /**
     * Generates a token.
     * @param size the size of the token
     * @return the generated token
     */
    public static String generateToken(int size) {
        byte[] randomBytes = new byte[size];
        secureRandom.nextBytes(randomBytes);
        String token = base64UrlEncoder.encodeToString(randomBytes);

        return token.replaceAll("\\W", "");
    }

    /**
     * Gets the signing key.
     * @param secret the secret
     * @return the signing key
     */
    private static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        byte[] base64KeyBytes = base64Encoder.encode(keyBytes);
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

    /**
     * Parses the token.
     * @param parser the parser
     * @param token the token
     * @return the parsed token
     */
    public static Jws<Claims> parseToken(JwtParser parser, String token) {
        return parser.parseClaimsJws(token);
    }
}
