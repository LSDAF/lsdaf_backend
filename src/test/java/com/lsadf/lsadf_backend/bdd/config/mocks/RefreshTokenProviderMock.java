package com.lsadf.lsadf_backend.bdd.config.mocks;

import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.impl.RefreshTokenProviderImpl;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.DateUtils;
import io.jsonwebtoken.JwtParser;

import java.time.Clock;
import java.util.Date;

public class RefreshTokenProviderMock extends RefreshTokenProviderImpl {
    public RefreshTokenProviderMock(UserService userService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    JwtParser parser,
                                    AuthProperties authProperties,
                                    ClockService clockService) {
        super(userService, refreshTokenRepository, parser, authProperties, clockService);
    }

    @Override
    public RefreshTokenEntity saveRefreshToken(UserEntity userEntity, String token) {
        Date now = clockService.nowDate();
        Date expiration = new Date(now.getTime() + authProperties.getRefreshTokenExpirationSeconds());
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
}
