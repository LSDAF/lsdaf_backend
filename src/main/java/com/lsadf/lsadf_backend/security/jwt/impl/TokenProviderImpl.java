package com.lsadf.lsadf_backend.security.jwt.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.ClockService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Date;

import static com.lsadf.lsadf_backend.utils.TokenUtils.generateToken;

/**
 * Implementation of the token provider.
 */
@Slf4j
public class TokenProviderImpl implements TokenProvider {

    private final AuthProperties authProperties;
    private final JwtParser parser;
    private final ClockService clockService;

    private final Cache<String> invalidatedJwtTokenCache;

    public TokenProviderImpl(AuthProperties authProperties,
                             JwtParser parser,
                             Cache<String> invalidatedJwtTokenCache,
                             ClockService clockService) {
        this.authProperties = authProperties;
        this.parser = parser;
        this.invalidatedJwtTokenCache = invalidatedJwtTokenCache;
        this.clockService = clockService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String createJwtToken(LocalUser localUser) {
        Date now = clockService.nowDate();
        return generateToken(localUser, authProperties.getTokenSecret(), authProperties.getTokenExpirationSeconds(), now);
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
            Jws<Claims> claims = this.parser.parseClaimsJws(authToken);
            if (invalidatedJwtTokenCache.get(authToken).isPresent()) {
                throw new InvalidTokenException("Token is invalidated");
            }
            return true;
        } catch (InvalidTokenException e) {
            log.error("Token has already been invalidated");
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

    @Override
    public void invalidateToken(String token, String userEmail) {
        invalidatedJwtTokenCache.set(token, userEmail);
        log.info("Token for user {} invalidated", userEmail);
    }
}
