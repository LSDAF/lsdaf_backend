package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of the User Controller
 */
@RestController(value = ControllerConstants.USER)
@Slf4j
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final Mapper mapper;

    public UserControllerImpl(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@CurrentUser LocalUser localUser) throws UnauthorizedException {
        if (localUser == null) {
            log.error("Didn't manage to build UserInfo from token. Please login");
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized. Didn't manage to build User info from token. Please login", null);
        }
        UserInfo userInfo = userService.buildUserInfoFromLocalUser(localUser);
        return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully retrieved user info", userInfo);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@CurrentUser LocalUser localUser) throws UnauthorizedException {
        if (localUser == null) {
            log.error("Didn't manage to build UserInfo from token. Please login");
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized. Didn't manage to build User info from token. Please login", null);
        }

        UserEntity user = userService.getUserByEmail(localUser.getEmail());
        List<GameSave> gameSaves = user.getGameSaves().stream().map(mapper::mapToGameSave).toList();

        return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully retrieved user game saves from user", gameSaves);
    }
}
