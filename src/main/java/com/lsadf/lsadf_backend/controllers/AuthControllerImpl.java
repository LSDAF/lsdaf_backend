package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.UserLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController(value = ControllerConstants.AUTH)
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthControllerImpl(AuthenticationManager authenticationManager,
                              UserService userService,
                              TokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<JwtAuthentication>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String email = (String) authentication.getPrincipal();
        LocalUser localUser = userDetailsService.loadUserByEmail(email);
        String jwt = tokenProvider.createToken(localUser);
        return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully logged in", new JwtAuthentication(jwt, userService.buildUserInfoFromLocalUser(localUser)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<UserInfo>> register(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        try {
            Optional<Set<UserRole>> roles = Optional.of(Sets.newHashSet(UserRole.getDefaultRole()));
            SocialProvider socialProvider = SocialProvider.getDefaultSocialProvider();
            UserEntity userEntity = userService.createUser(userCreationRequest.getName(), userCreationRequest.getEmail(), userCreationRequest.getPassword(), socialProvider, roles);
            UserInfo userInfo = userService.buildUserInfoFromUserEntity(userEntity);

            return ResponseUtils.generateResponse(HttpStatus.OK, "User registered successfully", userInfo);
        } catch (AlreadyExistingUserException e) {
            log.error("User with email {} already exists", userCreationRequest.getEmail(), e);
            return ResponseUtils.generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}
