package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.annotations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.controllers.AuthController;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.EmailService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.services.UserVerificationService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.TokenUtils.extractTokenFromHeader;


/**
 * Implementation of the Auth Controller
 */
@RestController(value = ControllerConstants.AUTH)
@Slf4j
public class AuthControllerImpl extends BaseController implements AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider<JwtTokenEntity> tokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserVerificationService userVerificationService;
    private final TokenProvider<RefreshTokenEntity> refreshTokenProvider;
    private final Mapper mapper;
    private final EmailService emailService;

    public AuthControllerImpl(AuthenticationManager authenticationManager,
                              UserService userService,
                              TokenProvider<JwtTokenEntity> tokenProvider,
                              UserDetailsService userDetailsService,
                              TokenProvider<RefreshTokenEntity> refreshTokenProvider,
                              Mapper mapper,
                              UserVerificationService userVerificationService,
                              EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userVerificationService = userVerificationService;
        this.userService = userService;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<JwtAuthentication>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) throws NotFoundException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LocalUser localUser = userDetailsService.loadUserByEmail(userLoginRequest.getEmail());
            boolean isValidPassword = userService.validateUserPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());
            if (!isValidPassword) {
                throw new WrongPasswordException("Invalid password");
            }
            checkUserEnabling(localUser);
            JwtTokenEntity jwt = tokenProvider.createToken(localUser);
            RefreshTokenEntity refreshToken = refreshTokenProvider.createToken(localUser);

            return generateResponse(HttpStatus.OK, new JwtAuthentication(jwt.getToken(), refreshToken.getToken(), mapper.mapLocalUserToUserInfo(localUser)));
        } catch (NotFoundException e) {
            log.error("User with email {} not found", userLoginRequest.getEmail(), e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (WrongPasswordException e) {
            log.error("Invalid password");
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Authentication failed: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Could not login user", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not login user!", null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<JwtAuthentication>> loginFromRefreshToken(@Valid @RequestBody UserRefreshLoginRequest userRefreshLoginRequest) throws NotFoundException {
        try {

            String userEmail = userRefreshLoginRequest.getEmail();

            LocalUser localUser = userDetailsService.loadUserByEmail(userEmail);
            checkUserEnabling(localUser);
            refreshTokenProvider.validateToken(userRefreshLoginRequest.getRefreshToken(), userEmail);

            // Create a new refresh token
            RefreshTokenEntity newRefreshToken = refreshTokenProvider.createToken(localUser);

            JwtTokenEntity jwt = tokenProvider.createToken(localUser);

            return generateResponse(HttpStatus.OK, new JwtAuthentication(jwt.getToken(), newRefreshToken.getToken(), mapper.mapLocalUserToUserInfo(localUser)));
        } catch (NotFoundException e) {
            log.error("User with email {} not found", userRefreshLoginRequest.getEmail(), e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (AuthenticationException | InvalidTokenException e) {
            log.error("Authentication failed", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Could not refresh token", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not refresh token", null);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> logout(@CurrentUser LocalUser localUser,
                                                        @Schema(hidden = true) @RequestHeader(name = "Authorization") String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            validateUser(localUser);
            tokenProvider.invalidateToken(token, localUser.getUserEntity().getEmail());

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("User is not authorized to logout", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Could not logout user", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not logout user", null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<UserInfo>> register(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        try {
            Set<UserRole> roles = Sets.newHashSet(UserRole.getDefaultRole());
            UserEntity userEntity = userService.createUser(null, userCreationRequest.getEmail(), userCreationRequest.getPassword(), userCreationRequest.getSocialProvider(), roles, userCreationRequest.getName(), false);
            UserVerificationTokenEntity userVerificationTokenEntity = userVerificationService.createUserValidationToken(userEntity);
            emailService.sendUserValidationEmail(userEntity.getEmail(), userVerificationTokenEntity.getToken());
            UserInfo userInfo = mapper.mapUserEntityToUserInfo(userEntity);

            return generateResponse(HttpStatus.OK, userInfo);
        } catch (AlreadyExistingUserException e) {
            log.error("User with email {} already exists", userCreationRequest.getEmail(), e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Could not create user", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create user: " + e.getMessage(), null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<UserInfo>> validateUserAccount(@RequestParam(value = TOKEN) String token) {
        try {
            UserEntity validatedUser = userVerificationService.validateUserVerificationToken(token);
            UserInfo userInfo = mapper.mapUserEntityToUserInfo(validatedUser);

            return generateResponse(HttpStatus.OK, userInfo);
        } catch (InvalidTokenException e) {
            log.error("InvalidTokenException: "+ e.getMessage(), e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("NotFoundException: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.info("Could not validate user account", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Could not validate user account: " + e.getMessage(), null);
        }
    }

    /**
     * Check if user is enabled
     * @param user user to check
     */
    private static void checkUserEnabling(LocalUser user) {
        if (!user.getEmailVerified()) {
            throw new DisabledException("Email is not verified");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
    }
}
