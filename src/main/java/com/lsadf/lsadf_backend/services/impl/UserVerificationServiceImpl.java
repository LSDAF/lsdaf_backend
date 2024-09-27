package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.TokenStatus;
import com.lsadf.lsadf_backend.properties.UserVerificationProperties;
import com.lsadf.lsadf_backend.repositories.UserVerificationTokenRepository;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.services.UserVerificationService;
import com.lsadf.lsadf_backend.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

/**
 * Implementation of the UserValidationService
 */
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserVerificationTokenRepository userVerificationTokenRepository;
    private final UserVerificationProperties userVerificationProperties;
    private final UserService userService;
    private final ClockService clockService;


    public UserVerificationServiceImpl(UserVerificationTokenRepository userVerificationTokenRepository,
                                       UserService userService,
                                       UserVerificationProperties userVerificationProperties,
                                       ClockService clockService) {
        this.userVerificationTokenRepository = userVerificationTokenRepository;
        this.userService = userService;
        this.userVerificationProperties = userVerificationProperties;
        this.clockService = clockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserVerificationTokenEntity createUserValidationToken(UserEntity userEntity) throws NotFoundException {
        if (!userService.existsByEmail(userEntity.getEmail())) {
            throw new NotFoundException("User not found");
        }

        String validationToken = TokenUtils.generateToken(24);
        long expirationDateLong = clockService.nowDate().getTime() + userVerificationProperties.getExpirationSeconds() * 1000L;
        Date expirationDate = new Date(expirationDateLong);

        UserVerificationTokenEntity userVerificationTokenEntity = new UserVerificationTokenEntity();
        userVerificationTokenEntity.setUser(userEntity);
        userVerificationTokenEntity.setToken(validationToken);
        userVerificationTokenEntity.setExpirationDate(expirationDate);
        userVerificationTokenEntity.setStatus(TokenStatus.ACTIVE);

        log.info("Created user validation token for user with email {}", userEntity.getEmail());

        return userVerificationTokenRepository.save(userVerificationTokenEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity validateUser(String token) throws NotFoundException, InvalidTokenException {
        Optional<UserVerificationTokenEntity> userValidationTokenEntityOptional = userVerificationTokenRepository.findUserValidationTokenEntityByValidationToken(token);
        if (userValidationTokenEntityOptional.isEmpty()) {
            throw new NotFoundException("Token not found");
        }

        UserVerificationTokenEntity userVerificationTokenEntity = userValidationTokenEntityOptional.get();

        TokenStatus tokenStatus = userVerificationTokenEntity.getStatus();

        if (tokenStatus.equals(TokenStatus.INVALIDATED)) {
            throw new InvalidTokenException("Token already used");
        }
        if (tokenStatus.equals(TokenStatus.EXPIRED)) {
            throw new InvalidTokenException("Token expired");
        }

        UserEntity user = userVerificationTokenEntity.getUser();

        UserEntity validatedUser = userService.validateUser(user.getEmail());

        userVerificationTokenEntity.setStatus(TokenStatus.INVALIDATED);
        userVerificationTokenEntity.setUser(validatedUser);

        userVerificationTokenRepository.save(userVerificationTokenEntity);

        log.info("User with email {} has been validated", user.getEmail());

        return validatedUser;
    }
}
