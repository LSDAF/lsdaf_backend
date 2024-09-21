package com.lsadf.lsadf_backend.security.jwt.impl;


import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.RefreshTokenProvider;
import com.lsadf.lsadf_backend.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.lsadf.lsadf_backend.utils.TokenUtils.generateToken;

@Slf4j
public class RefreshTokenProviderImpl implements RefreshTokenProvider {

    protected final RefreshTokenRepository refreshTokenRepository;
    protected final UserService userService;
    protected final JwtParser parser;
    protected final AuthProperties authProperties;

    public RefreshTokenProviderImpl(UserService userService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    JwtParser parser,
                                    AuthProperties authProperties) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.parser = parser;
        this.authProperties = authProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RefreshTokenEntity saveRefreshToken(UserEntity userEntity, String token) {
        Date now = new Date();
        Jws<Claims> claims = parser.parseClaimsJws(token);
        Date expiration = claims.getBody().getExpiration();
        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .token(token)
                .expirationDate(expiration)
                .status(RefreshTokenEntity.Status.ACTIVE)
                .user(userEntity)
                .build();

        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return refreshTokenRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void invalidateUserRefreshToken(String userEmail) throws NotFoundException {

        UserEntity user = userService.getUserByEmail(userEmail);

        Optional<RefreshTokenEntity> tokenEntityOptional = refreshTokenRepository.findByUserAndStatus(user, RefreshTokenEntity.Status.ACTIVE);
        if (tokenEntityOptional.isEmpty()) {
            return;
        }

        RefreshTokenEntity tokenEntity = tokenEntityOptional.get();
        invalidateToken(tokenEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createRefreshToken(LocalUser localUser) {
        return generateToken(localUser, authProperties.getRefreshTokenSecret(), authProperties.getRefreshTokenExpirationSeconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteExpiredTokens() {
        int count = 0;

        Iterable<RefreshTokenEntity> tokensIterable = refreshTokenRepository.findAllByStatus(RefreshTokenEntity.Status.INACTIVE);

        for (RefreshTokenEntity token : tokensIterable) {
            refreshTokenRepository.deleteById(token.getId());
            count++;
        }

        log.info("Deleted {} expired tokens", count);
    }

    /**
     * Invalidates a token.
     * @param entity the entity to invalidate
     */
    private void invalidateToken(RefreshTokenEntity entity) {
        Date now = new Date();
        entity.setStatus(RefreshTokenEntity.Status.INACTIVE);
        entity.setInvalidationDate(now);
        entity.setUpdatedAt(now);
        refreshTokenRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void validateRefreshToken(String token, String userEmail) throws NotFoundException, InvalidTokenException {
        Date now = new Date();
        Optional<RefreshTokenEntity> tokenEntityOptional = refreshTokenRepository.findByToken(token);
        if (tokenEntityOptional.isEmpty()) {
            throw new NotFoundException("Refresh token not found");
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
    }

    @Scheduled(cron = "${auth.invalidated-token-cleaner-cron}")
    @Transactional
    public void cleanInvalidatedTokens() {
        log.info("Cleaning expired & invalidated tokens");
        deleteExpiredTokens();
    }

    private static boolean isInvalidated(RefreshTokenEntity tokenEntity) {
        return tokenEntity.getStatus() == RefreshTokenEntity.Status.INACTIVE;
    }

    private static boolean isExpired(RefreshTokenEntity tokenEntity, Date date) {
        return tokenEntity.getExpirationDate().before(date);
    }
}
