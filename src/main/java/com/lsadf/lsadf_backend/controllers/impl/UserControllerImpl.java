package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.controllers.UserController;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;


/**
 * Implementation of the User Controller
 */
@RestController
@Slf4j
public class UserControllerImpl extends BaseController implements UserController {
    private final UserService userService;
    private final Mapper mapper;
    private final GameSaveService gameSaveService;

    public UserControllerImpl(UserService userService, Mapper mapper, GameSaveService gameSaveService) {
        this.userService = userService;
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
    @Transactional(readOnly = true)
    public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            UserInfo userInfo = mapper.mapLocalUserToUserInfo(localUser);
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
    public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            String email = localUser.getUsername();

            List<GameSave> gameSaveList = gameSaveService.getGameSavesByUserEmail(email)
                    .stream()
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
