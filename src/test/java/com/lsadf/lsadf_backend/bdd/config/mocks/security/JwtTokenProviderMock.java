package com.lsadf.lsadf_backend.bdd.config.mocks.security;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.JwtTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.jwt.impl.JwtTokenProviderImpl;
import com.lsadf.lsadf_backend.services.ClockService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenProviderMock extends JwtTokenProviderImpl implements TokenProvider<JwtTokenEntity> {

    private Map<String, Pair<Date, LocalUser>> localUserMap;

    public JwtTokenProviderMock(AuthProperties authProperties,
                                JwtParser parser,
                                Cache<String> invalidatedJwtTokenCache,
                                ClockService clockService,
                                Map<String, Pair<Date, LocalUser>> localUserMap,
                                JwtTokenRepository jwtTokenRepository) {
        super(authProperties, parser, invalidatedJwtTokenCache, clockService, jwtTokenRepository);
        this.localUserMap = localUserMap;
    }

    @Override
    public void invalidateToken(String token, String userEmail) {
        var pair = localUserMap.get(token);
        Date now = clockService.nowDate();
        long expirationLong = pair.getLeft().getTime() + authProperties.getTokenExpirationSeconds() * 1000L;
        long expirationSeconds = (expirationLong - now.getTime()) / 1000;
        int expirationSecondsInt = (int) expirationSeconds;
        invalidatedJwtTokenCache.set(token, userEmail, expirationSecondsInt);
    }

    @Override
    public boolean validateToken(String authToken, String userEmail) {
        try {
            if (invalidatedJwtTokenCache.get(authToken).isPresent()) {
                throw new InvalidTokenException("Token has already been invalidated");
            }
            return true;
        } catch (InvalidTokenException e) {
            log.error(e.getMessage());
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
