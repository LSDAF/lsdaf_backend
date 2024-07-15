package com.lsadf.lsadf_backend.security.jwt;

import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Implementation of the token provider.
 */
@Slf4j
public class TokenProviderImpl implements TokenProvider {

    private final AuthProperties authProperties;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public TokenProviderImpl(AuthProperties authProperties,
                             UserService userService,
                             UserDetailsService userDetailsService) {
        this.authProperties = authProperties;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String createToken(LocalUser localUser) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + authProperties.getTokenExpirationMs());

        return Jwts.builder()
                .setSubject(localUser.getUserEntity().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .setIssuedAt(now)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.authProperties.getTokenSecret().getBytes(StandardCharsets.UTF_8);
        byte[] base64KeyBytes = Base64.getEncoder().encode(keyBytes);
        return Keys.hmacShaKeyFor(base64KeyBytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encode(authProperties.getTokenSecret().getBytes()))
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Base64.getEncoder().encode(authProperties.getTokenSecret().getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
