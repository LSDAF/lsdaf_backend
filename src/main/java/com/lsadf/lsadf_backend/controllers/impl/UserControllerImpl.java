package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.UserController;
import com.lsadf.lsadf_backend.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;


/**
 * Implementation of the User Controller
 */
@RestController
@Slf4j
public class UserControllerImpl extends BaseController implements UserController {
    private final Mapper mapper;
    private final GameSaveService gameSaveService;

    public UserControllerImpl( Mapper mapper, GameSaveService gameSaveService) {
        this.mapper = mapper;
        this.gameSaveService = gameSaveService;
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
    public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        try {
            String username = jwt.getClaimAsString("preferred_username");
            String name = jwt.getClaimAsString("name");
            boolean verified = jwt.getClaimAsBoolean("email_verified");
            List<GrantedAuthority> authorities = TokenUtils.getRolesFromClaims(jwt.getClaims());
            Set<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

            UserInfo userInfo = new UserInfo(name, username, verified, roles);
            return generateResponse(HttpStatus.OK, userInfo);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user info: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting user info", null);
        } catch (Exception e) {
            log.error("Exception {} while getting user info: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting user info", null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@AuthenticationPrincipal Jwt jwt) {
        try {
            validateUser(jwt);
            String email = jwt.getSubject();

            List<GameSave> gameSaveList = gameSaveService.getGameSavesByUserEmail(email)
                    .map(mapper::mapToGameSave)
                    .toList();

            return generateResponse(HttpStatus.OK, gameSaveList);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user game saves: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting user game saves", null);
        } catch (Exception e) {
            log.error("Exception {} while getting user game saves: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting user game saves", null);
        }
    }
}
