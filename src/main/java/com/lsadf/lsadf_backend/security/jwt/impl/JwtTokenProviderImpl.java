package com.lsadf.lsadf_backend.security.jwt.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.TokenStatus;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.JwtTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.utils.TokenUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.utils.TokenUtils.generateToken;

/**
 * Implementation of the token provider.
 */
@Slf4j
public class JwtTokenProviderImpl implements TokenProvider<JwtTokenEntity> {

    protected final AuthProperties authProperties;
    protected final JwtParser parser;
    protected final ClockService clockService;

    protected final Cache<String> invalidatedJwtTokenCache;
    protected final JwtTokenRepository jwtTokenRepository;

    public JwtTokenProviderImpl(AuthProperties authProperties,
                                JwtParser parser,
                                Cache<String> invalidatedJwtTokenCache,
                                ClockService clockService,
                                JwtTokenRepository jwtTokenRepository) {
        this.authProperties = authProperties;
        this.parser = parser;
        this.invalidatedJwtTokenCache = invalidatedJwtTokenCache;
        this.clockService = clockService;
        this.jwtTokenRepository = jwtTokenRepository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public JwtTokenEntity createToken(LocalUser localUser) {
        Date now = clockService.nowDate();
        String token = generateToken(localUser, authProperties.getTokenSecret(), authProperties.getTokenExpirationSeconds(), now);
        JwtTokenEntity entity = new JwtTokenEntity();
        entity.setToken(token);
        entity.setUser(localUser.getUserEntity());
        entity.setExpirationDate(new Date(now.getTime() + authProperties.getTokenExpirationSeconds() * 1000L));
        entity.setStatus(TokenStatus.ACTIVE);

        return jwtTokenRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserEmailFromToken(String token) {
        Jws<Claims> claims = TokenUtils.parseToken(parser, token);

        return claims.getBody().getSubject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String authToken, String userEmail) {
        try {
            TokenUtils.parseToken(parser, authToken);
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

    @Override
    @Transactional
    public void invalidateToken(String token, String userEmail) throws NotFoundException {
        Jws<Claims> claims = TokenUtils.parseToken(parser, token);
        Date expiration = claims.getBody().getExpiration();
        Date now = clockService.nowDate();
        long expirationSeconds = (expiration.getTime() - now.getTime()) / 1000;
        int expirationSecondsInt = (int) expirationSeconds;
        invalidatedJwtTokenCache.set(token, userEmail, expirationSecondsInt);

        JwtTokenEntity jwtTokenEntity = jwtTokenRepository.findByToken(token).orElseThrow(NotFoundException::new);
        jwtTokenEntity.setStatus(TokenStatus.INVALIDATED);
        jwtTokenEntity.setInvalidationDate(now);

        jwtTokenRepository.save(jwtTokenEntity);

        log.info("Token for user {} invalidated", userEmail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<JwtTokenEntity> getInvalidatedTokens() {
        List<JwtTokenEntity> result = new ArrayList<>();
        Date now = clockService.nowDate();
        try (Stream<JwtTokenEntity> stream = jwtTokenRepository.findAllByStatusAndExpirationDateAfter(TokenStatus.INVALIDATED, now)) {
            stream.forEach(result::add);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteExpiredTokens() {
        int count = 0;

        Date now = clockService.nowDate();

        try (Stream<JwtTokenEntity> tokenStream = jwtTokenRepository.findAllByExpirationDateBefore(now)) {
            tokenStream.forEach(token -> jwtTokenRepository.deleteById(token.getId()));
            count++;
        }

        log.info("Deleted {} expired tokens", count);
    }
}
