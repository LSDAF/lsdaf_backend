package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.impl.RefreshTokenProviderImpl;
import com.lsadf.lsadf_backend.services.UserService;
import io.jsonwebtoken.JwtParser;

import java.util.Date;

public class RefreshTokenProviderMock extends RefreshTokenProviderImpl {
    public RefreshTokenProviderMock(UserService userService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    JwtParser parser,
                                    AuthProperties authProperties) {
        super(userService, refreshTokenRepository, parser, authProperties);
    }

    @Override
    public RefreshTokenEntity saveRefreshToken(UserEntity userEntity, String token) {
        Date now = new Date();
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
