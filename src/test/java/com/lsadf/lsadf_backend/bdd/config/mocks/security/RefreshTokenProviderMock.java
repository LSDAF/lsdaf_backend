package com.lsadf.lsadf_backend.bdd.config.mocks.security;

import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.TokenStatus;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.security.jwt.impl.RefreshTokenProviderImpl;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import io.jsonwebtoken.JwtParser;

import java.util.Date;

import static com.lsadf.lsadf_backend.utils.TokenUtils.generateToken;

public class RefreshTokenProviderMock extends RefreshTokenProviderImpl {


    public RefreshTokenProviderMock(UserService userService,
                                    RefreshTokenRepository refreshTokenRepository,
                                    JwtParser parser,
                                    AuthProperties authProperties,
                                    ClockService clockService) {
        super(userService, refreshTokenRepository, parser, authProperties, clockService);
    }

    @Override
    public RefreshTokenEntity createToken(LocalUser localUser) {
        Date now = clockService.nowDate();
        Date expiration = new Date(now.getTime() + authProperties.getRefreshTokenExpirationSeconds());
        String token = generateToken(localUser, authProperties.getTokenSecret(), authProperties.getTokenExpirationSeconds(), now);
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(localUser.getUserEntity());
        entity.setToken(token);
        entity.setStatus(TokenStatus.ACTIVE);
        entity.setExpirationDate(expiration);

        return refreshTokenRepository.save(entity);
    }
}
