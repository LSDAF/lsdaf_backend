package com.lsadf.lsadf_backend.security.jwt.impl;


import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.models.TokenStatus.*;
import static com.lsadf.lsadf_backend.utils.TokenUtils.generateToken;

@Slf4j
public class RefreshTokenProviderImpl implements TokenProvider<RefreshTokenEntity> {

    protected final RefreshTokenRepository refreshTokenRepository;
    protected final UserService userService;
    protected final JwtParser parser;
    protected final AuthProperties authProperties;
    protected final ClockService clockService;

    public RefreshTokenProviderImpl(UserService userService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    JwtParser parser,
                                    AuthProperties authProperties,
                                    ClockService clockService) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.parser = parser;
        this.authProperties = authProperties;
        this.clockService = clockService;
    }

    @Override
    public RefreshTokenEntity createToken(LocalUser localUser) {
        UserEntity userEntity = localUser.getUserEntity();
        Date now = clockService.nowDate();
        String token = generateToken(localUser, authProperties.getTokenSecret(), authProperties.getTokenExpirationSeconds(), now);
        Jws<Claims> claims = TokenUtils.parseToken(parser, token);
        Date expiration = claims.getBody().getExpiration();
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(userEntity);
        entity.setToken(token);
        entity.setExpirationDate(expiration);
        entity.setStatus(ACTIVE);

        return refreshTokenRepository.save(entity);
    }

    @Override
    public String getUserEmailFromToken(String token) {
        return "";
    }

    @Override
    public List<JwtTokenEntity> getInvalidatedTokens() {
        return List.of();
    }


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void invalidateToken(String token, String userEmail) throws NotFoundException, InvalidTokenException {

        UserEntity user = userService.getUserByEmail(userEmail);

        Optional<RefreshTokenEntity> tokenEntityOptional = refreshTokenRepository.findByToken(token);
        if (tokenEntityOptional.isEmpty()) {
            return;
        }
        if (!tokenEntityOptional.get().getUser().equals(user)) {
            throw new InvalidTokenException("The token does not belong to the user");
        }

        RefreshTokenEntity tokenEntity = tokenEntityOptional.get();
        invalidateToken(tokenEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteExpiredTokens() {
        int count = 0;

        Date now = clockService.nowDate();

        try (Stream<RefreshTokenEntity> tokenStream = refreshTokenRepository.findAllByExpirationDateBefore(now)) {
            tokenStream.forEach(token -> refreshTokenRepository.deleteById(token.getId()));
            count++;
        }

        log.info("Deleted {} expired tokens", count);
    }

    /**
     * Invalidates a token.
     *
     * @param entity the entity to invalidate
     */
    private void invalidateToken(RefreshTokenEntity entity) {
        Date now = clockService.nowDate();
        entity.setStatus(INVALIDATED);
        entity.setInvalidationDate(now);
        entity.setUpdatedAt(now);
        refreshTokenRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(String token, String userEmail) throws NotFoundException, InvalidTokenException {
        Date now = clockService.nowDate();
        Optional<RefreshTokenEntity> tokenEntityOptional = refreshTokenRepository.findByToken(token);
        if (tokenEntityOptional.isEmpty()) {
            throw new NotFoundException("Refresh token not found");
        }

        if (userEmail == null) {
            throw new IllegalArgumentException("User email is required");
        }

        RefreshTokenEntity tokenEntity = tokenEntityOptional.get();

        if (!tokenEntity.getUser().getEmail().equals(userEmail)) {
            throw new InvalidTokenException("The refresh token does not belong to the user");
        }

        if (isExpired(tokenEntity, now)) {
            invalidateToken(tokenEntity);
            throw new InvalidTokenException("Refresh token expired");
        }

        if (isInvalidated(tokenEntity)) {
            throw new InvalidTokenException("Refresh token invalidated");
        }

        return true;
    }

    private static boolean isInvalidated(RefreshTokenEntity tokenEntity) {
        return tokenEntity.getStatus() == INVALIDATED;
    }

    private static boolean isExpired(RefreshTokenEntity tokenEntity, Date date) {
        return tokenEntity.getExpirationDate().before(date);
    }
}
